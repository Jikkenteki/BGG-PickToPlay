(ns bbg-reframe.subs
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.game-view.subs :as game-subs]
   [bbg-reframe.events :as events]))

(re-frame/reg-sub
 ::db
 (fn [db]
   db))

(re-frame/reg-sub
 ::games
 (fn [db]
   (:games db)))

(re-frame/reg-sub
 ::game
 :<- [::games]
 (fn [games [_ id]]
   (get games id)))

(re-frame/reg-sub
 ::result
 (fn [db]
   (:result db)))

;; (re-frame/reg-sub
;;  ::fields
;;  (fn [db]
;;    (:fields db)))

(re-frame/reg-sub
 ::form
 (fn [db [_ id]]
   (get-in db [:form id])))

(re-frame/reg-sub
 ::loading
 (fn [db]
   (:loading db)))

(re-frame/reg-sub
 ::error-msg
 (fn [db]
   (:error db)))

(re-frame/reg-sub
 ::user
 (fn [db]
   (:user db)))

(re-frame/reg-sub
 ::substring-query
 (fn [db]
   (:substring db)))

(re-frame/reg-sub
 ::search-results
 (fn [db]
   (:search-results db)))

(re-frame/reg-sub
 ::ui
 (fn [db [_ id]]
   (get-in db [:ui id])))


;; routing

;; (re-frame/reg-sub
;;  ::active-panel
;;  (fn [db _]
;;    (:active-panel db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (get-in db [:route :panel])))

(re-frame/reg-sub
 ::route-params
 (fn [db _]
   (get-in db [:route :route-params])))

(re-frame/reg-sub
 ::available-games
 :<- [::game-subs/on-auth-value ["available"]]
 (fn [games]
   (re-frame/dispatch [::events/make-available games])
   games))

