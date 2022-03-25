(ns bbg-reframe.game-view.subs
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.subs :as subs]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [bbg-reframe.forms.events :as form-events]
            [bbg-reframe.forms.utils :refer [if-nil?->value]]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(re-frame/reg-sub
 ::on-auth-value
 :<- [::fb-reframe/on-auth-state-changed]
 (fn [uid [_ path]]
   (if-nil?->value @(re-frame/subscribe [::fb-reframe/on-value (concat ["users" uid] path)]) nil)))


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
 (fn [value [_ id form-path]]
  ;;  (println "::available " id available-games)
   (let [value ((keyword id) value)]
     (re-frame/dispatch [::form-events/set-value! form-path value])
     value)))

;;
;; grouping (for boxes with games)
;;
(re-frame/reg-sub
 ::group-with
 :<- [::on-auth-value ["group-with"]]
 (fn [value [_ id  form-path]]
   (let [val ((keyword id) value)]
     (re-frame/dispatch [::form-events/set-value! form-path val])
     val)))

(re-frame/reg-sub
 ::comment
 :<- [::on-auth-value ["comment"]]
 (fn [value [_ id  form-path]]
   (let [val ((keyword id) value)]
     (re-frame/dispatch [::form-events/set-value! form-path val])
     val)))


