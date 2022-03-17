(ns bbg-reframe.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::games
 (fn [db]
   (:games db)))

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
   (let [value (get-in db [:form id])]
     value)))

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
 ::email
 (fn [db]
   (:email db)))