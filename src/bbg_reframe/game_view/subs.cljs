(ns bbg-reframe.game-view.subs
  (:require [re-frame.core :as re-frame]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [bbg-reframe.forms.utils :refer [if-nil?->value]]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(re-frame/reg-sub
 ::on-auth-value
 :<- [::fb-reframe/on-auth-state-changed]
 (fn [uid [_ path]]
   (if-nil?->value @(re-frame/subscribe [::fb-reframe/on-value (concat ["users" uid] path)]) nil)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FIREBASE
;;


(defn if-not-nil->get-value-with-keyword-id
  [value [_ id]]
  (if (nil? value) nil
      (let [value ((keyword id) value)]
        value)))
;;
;; available games
;;
(re-frame/reg-sub
 ::available
 :<- [::on-auth-value ["available"]]
 if-not-nil->get-value-with-keyword-id)

;;
;; grouping (for boxes with games)
;;
(re-frame/reg-sub
 ::group-with
 :<- [::on-auth-value ["group-with"]]
 if-not-nil->get-value-with-keyword-id)

(re-frame/reg-sub
 ::comment
 :<- [::on-auth-value ["comment"]]
 if-not-nil->get-value-with-keyword-id)

(re-frame/reg-sub
 ::game-info
 (fn [[_ id]]
   [(re-frame/subscribe [::available id])
    (re-frame/subscribe [::group-with id])
    (re-frame/subscribe [::comment id])])
 (fn [[available group-with comment] [_ id]]
   {:id id :available available :group-with group-with :comment comment}))
