(ns bbg-reframe.game-view.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing-stubs :refer [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]))

(re-frame/reg-event-fx
 ::save-game
 (fn-traced [{:keys [db]} [_ id]]
            {::fb-reframe/firebase-update {:path ["users" (fb-reframe/get-current-user-uid)]
                                           :path-data-map {["available" id] (if-let [v (get-in db [:game-form :available id])] v nil)
                                                           ["group-with" id] (get-in db [:game-form :group-with id])
                                                        ;;    ["collections" (get-in db [:game-form :add-to-collection id]) "games" id] true
                                                           }
                                           :success #(println "Success")}}))

(comment

  (fb-reframe/get-current-user-uid))