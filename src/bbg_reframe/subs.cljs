(ns bbg-reframe.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::db
 (fn [db]
   db))

;;
;; generic subscriber to any path in the db
;;
(re-frame/reg-sub
 ::db-path
 (fn [db [_ path]]
   (get-in db path)))

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

(re-frame/reg-sub
 ::form
 (fn [db [_ id]]
   (get-in db [:form id])))

(re-frame/reg-sub
 ::error-msg
 (fn [db]
   (:error db)))

(re-frame/reg-sub
 ::user
 (fn [db]
   (:user db)))

(re-frame/reg-sub
 ::ui
 (fn [db [_ id]]
   (get-in db [:ui id])))

(re-frame/reg-sub
 ::has-search-results?
 (fn [db _]
   (> (count (get-in db [:search :search-results])) 0)))


;; routing

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (get-in db [:route :panel])))

(re-frame/reg-sub
 ::route-params
 (fn [db _]
   (get-in db [:route :params])))

(re-frame/reg-sub
 ::route-path
 (fn [db _]
   (get-in db [:route :path])))
