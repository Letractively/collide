// Copyright 2012 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.collide.client.document;

import com.google.collide.client.AppContext;
import com.google.collide.client.communication.FrontendApi.ApiCallback;
import com.google.collide.client.document.DocumentManager.GetDocumentCallback;
import com.google.collide.client.status.StatusMessage;
import com.google.collide.client.status.StatusMessage.MessageType;
import com.google.collide.client.util.PathUtil;
import com.google.collide.client.util.logging.Log;
import com.google.collide.dto.FileContents.ContentType;
import com.google.collide.dto.GetFileContentsResponse;
import com.google.collide.dto.RoutingTypes;
import com.google.collide.dto.ServerError.FailureReason;
import com.google.collide.dto.client.DtoClientImpls.GetFileContentsImpl;
import com.google.collide.json.shared.JsonArray;
import com.google.collide.json.shared.JsonStringMap;
import com.google.collide.shared.util.JsonCollections;
import com.google.common.base.Preconditions;

/**
 * Controller responsible for loading documents (and uneditable files) from the
 * server.
 * 
 * This class accepts multiple calls to
 * {@link #load(PathUtil, GetDocumentCallback)} for the same path and intelligently
 * batches together the callbacks so only one network request will occur.
 */
class DocumentManagerNetworkController {

  private final DocumentManager documentManager;
  private final AppContext appContext;

  /** Path to a list of {@link GetDocumentCallback} */
  JsonStringMap<JsonArray<GetDocumentCallback>> outstandingCallbacks = JsonCollections.createMap();

  private StatusMessage loadingMessage;

  DocumentManagerNetworkController(DocumentManager documentManager, AppContext appContext) {
    this.documentManager = documentManager;
    this.appContext = appContext;
  }

  public void teardown() {
    if (loadingMessage != null) {
      cancelLoadingMessage();
    }
    
    appContext.getMessageFilter().removeMessageRecipient(RoutingTypes.GETFILECONTENTSRESPONSE);
  }

  void load(PathUtil path, GetDocumentCallback callback) {
    boolean shouldRequestFile = addCallback(path, callback);

    if (shouldRequestFile) {
      requestFile(path);
    }
  }

  private boolean addCallback(PathUtil path, GetDocumentCallback callback) {
    JsonArray<GetDocumentCallback> callbacks = outstandingCallbacks.get(path.getPathString());

    if (callbacks == null) {
      callbacks = JsonCollections.createArray(callback);
      outstandingCallbacks.put(path.getPathString(), callbacks);
      return true;

    } else {
      callbacks.add(callback);
      return false;
    }
  }

  private void requestFile(final PathUtil path) {
    delayLoadingMessage(path);
    
    // Fetch the file's contents
    GetFileContentsImpl getFileContents = GetFileContentsImpl.make().setPath(path.getPathString());
    appContext.getFrontendApi().GET_FILE_CONTENTS.send(getFileContents, 
        new ApiCallback<GetFileContentsResponse>() {

          @Override
          public void onMessageReceived(GetFileContentsResponse response) {
              handleFileReceived(response);
          }

          @Override
          public void onFail(FailureReason reason) {
            Log.error(getClass(), "Failed to retrieve file contents for path " + path);
          }
        });
  }

  /**
   * Called when the file contents are received from the network. Routes to the appropriate content
   * handling mechanism depending on whether or not the file content type is text, an image, or some
   * other binary file.
   */
  private void handleFileReceived(GetFileContentsResponse response) {
    boolean isUneditable =
        (response.getFileContents().getContentType() == ContentType.UNKNOWN_BINARY)
        || (response.getFileContents().getContentType() == ContentType.IMAGE);
    PathUtil path = new PathUtil(response.getFileContents().getPath());

    cancelLoadingMessage();

    JsonArray<GetDocumentCallback> callbacks = outstandingCallbacks.remove(path.getPathString());
    Preconditions.checkNotNull(callbacks);

    if (!response.getFileExists()) {
      // Dispatch to callbacks directly
      for (int i = 0, n = callbacks.size(); i < n; i++) {
        callbacks.get(i).onFileNotFoundReceived();
      }
    } else if (isUneditable) {
      // Dispatch to callbacks directly
      for (int i = 0, n = callbacks.size(); i < n; i++) {
        callbacks.get(i).onUneditableFileContentsReceived(response.getFileContents());
      }
    } else {
      documentManager.handleEditableFileReceived(response.getFileContents(), callbacks);
    }
  }

  private void delayLoadingMessage(PathUtil path) {
    cancelLoadingMessage();
    loadingMessage =
        new StatusMessage(appContext.getStatusManager(), MessageType.LOADING, "Loading "
            + path.getBaseName() + "...");
    loadingMessage.fireDelayed(StatusMessage.DEFAULT_DELAY);
  }

  private void cancelLoadingMessage() {
    if (loadingMessage != null) {
      loadingMessage.cancel();
      loadingMessage = null;
    }
  }
}
