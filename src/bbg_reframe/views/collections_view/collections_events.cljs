(ns bbg-reframe.views.collections-view.collections-events
  (:require [bbg-reframe.forms.forms :refer [db-get-ref]]
            [bbg-reframe.forms.events :as form-events]
            [bbg-reframe.model.collections :refer [add-if-not-exists]]
            [bbg-reframe.network-events :as events]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
 ::new-collection
 (fn-traced [_ [_ form-path]]
            (let [user-id (fb-reframe/get-current-user-uid)]
              (if (nil? user-id)
                {:dispatch [::events/set-error "Login to save collections"]}
                (if (not (nil? (add-if-not-exists (:new-collection @(db-get-ref form-path)))))
                  {:dispatch [::form-events/set-value! form-path {}]}
                  {:dispatch [::events/set-error "Collection with this name already exists!"]})))))