(ns bbg-reframe.game-view.subs
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.subs :as subs]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [bbg-reframe.forms.events :as form-events]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(re-frame/reg-sub
 ::on-auth-value
 :<- [::fb-reframe/on-auth-state-changed]
 (fn [uid [_ path]]
   @(re-frame/subscribe [::fb-reframe/on-value (concat ["users" uid] path)])))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(re-frame/reg-sub
 ::game
 :<- [::subs/games]
 (fn [games [_ id]]
   (get games id)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FIREBASE
;;

;;
;; available games
;;
(re-frame/reg-sub
 ::available
 :<- [::on-auth-value ["available"]]
 (fn [available-games [_ id]]
  ;;  (println "::available " id available-games)
   (let [value ((keyword id) available-games)]
     (re-frame/dispatch [::form-events/set-value! [:game-form :available id] value])
     value)))

;;
;; grouping (for boxes with games)
;;
(re-frame/reg-sub
 ::group-with
 :<- [::on-auth-value ["group-with"]]
 (fn [value [_ id]]
   (let [val ((keyword id) value)]
     (re-frame/dispatch [::form-events/set-value! [:game-form :group-with id] val])
     val)))
