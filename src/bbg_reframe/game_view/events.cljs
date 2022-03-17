(ns bbg-reframe.game-view.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [bbg-reframe.network-events :as events]))

(re-frame/reg-event-fx
 ::save-game
 (fn-traced [{:keys [db]} [_ id]]
            (let [user-id (fb-reframe/get-current-user-uid)]
              (if (nil? user-id)
                {:db (assoc db :error "Login to save extra info about games")
                 :dispatch-later {:ms 1000
                                  :dispatch [::events/reset-error]}}
                {::fb-reframe/firebase-update {:path ["users" user-id]
                                               :path-data-map {["available" id] (if-let [v (get-in db [:game-form :available id])] v nil)
                                                               ["group-with" id] (get-in db [:game-form :group-with id])
                                                        ;;    ["collections" (get-in db [:game-form :add-to-collection id]) "games" id] true
                                                               }
                                               :success #(println "Success")}}))))

(comment

  (fb-reframe/get-current-user-uid))