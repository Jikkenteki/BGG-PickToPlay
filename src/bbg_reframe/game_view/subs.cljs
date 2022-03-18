(ns bbg-reframe.game-view.subs
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.subs :as subs]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe :refer [get-current-user-uid]]
            [bbg-reframe.forms.events :as form-events]))

(re-frame/reg-sub
 ::game
 :<- [::subs/games]
 (fn [games [_ id]]
   (get games id)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FIREBASE
;;

(defn fb-sub-user-id
  [path]
  (re-frame/subscribe [::fb-reframe/on-value (concat ["users" (get-current-user-uid)] path)]))

;;
;; available games
;;
(re-frame/reg-sub
 ::available-games
 (fn []
   (fb-sub-user-id ["available"]))
 (fn [value]
   value))

(re-frame/reg-sub
 ::available
 :<- [::available-games]
 (fn [available-games [_ id]]
  ;;  (println "::available " id available-games)
   (let [value ((keyword id) available-games)]
     (re-frame/dispatch [::form-events/set-value! [:game-form :available id] value])
     value)))

;;
;; grouping (for boxes with games)
;;
(re-frame/reg-sub
 ::group-with-all
 (fn []
   (fb-sub-user-id ["group-with"]))
 (fn [value]
   value))

(re-frame/reg-sub
 ::group-with
 :<- [::group-with-all]
 (fn [value [_ id]]
   (let [val ((keyword id) value)]
     (re-frame/dispatch [::form-events/set-value! [:game-form :group-with id] val])
     val)))
