(ns bbg-reframe.game-view.subs
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.subs :as subs]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe :refer [get-current-user-uid]]
            [bbg-reframe.forms.events :as form-events]
            [re-frame-firebase-nine.firebase-database :refer [on-value off]]
            [re-frame-firebase-nine.utils :refer [if-vector?->map]]
            [reagent.ratom :as ratom]
            [re-frame.utils :refer [dissoc-in]]
            [re-frame-firebase-nine.firebase-auth :refer [on-auth-state-changed]]))



;;;;;;;;;;;;;;;;;;;;;;;;

(def temp-path-atom (atom [:temp]))

;; temp storage for fire-base reads
(re-frame/reg-event-db
 ::fb-write-to-temp
 (fn [db [_ path data]]
   (assoc-in db (concat @temp-path-atom path) data)))

;; clean temp storage
(re-frame/reg-event-db
 ::cleanup-temp
 (fn [db [_ path]]
   (dissoc-in db (concat @temp-path-atom path))))


(re-frame/reg-sub-raw
 ::on-auth-state-changed
 (fn [app-db [_]]
   (let [_ (on-auth-state-changed
            #(re-frame/dispatch [::fb-write-to-temp [:uid] (js->clj (.-uid %))]))]
     (ratom/make-reaction
      (fn [] (get-in @app-db (concat @temp-path-atom [:uid])))))))


(re-frame/reg-sub-raw
 ::on-value
 (fn [app-db [_ path]]
   (let [query-token (on-value path
                               #(re-frame/dispatch [::fb-write-to-temp path (if-vector?->map %)]))]
     (ratom/make-reaction
      (fn [] (get-in @app-db (concat @temp-path-atom path)))
      :on-dispose #(do (off path query-token)
                       (re-frame/dispatch [::cleanup-temp path]))))))


(re-frame/reg-sub-raw
 ::auth-on-value
 (fn [app-db [_ path]]
   (let [_ (on-auth-state-changed
            (fn [user]
              (if user
                (on-value path
                          #(re-frame/dispatch [::fb-write-to-temp path (if-vector?->map %)]))
                ()))
            ;; #(re-frame/dispatch [::fb-write-to-temp [:email] (js->clj (.-email %))])
            )]
     (ratom/make-reaction
      (fn [] (get-in @app-db (concat @temp-path-atom path)))
      ;; :on-dispose #(do (off path query-token)
      ;;                  (re-frame/dispatch [::cleanup-temp path]))
      ))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(re-frame/reg-sub
 ::on-auth-value
 :<- [::on-auth-state-changed]
 (fn [uid [_ path]]
   @(re-frame/subscribe [::on-value (concat ["users" uid] path)])))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
  (re-frame/subscribe [::on-value (concat ["users" (get-current-user-uid)] path)]))

;;
;; available games
;;
(re-frame/reg-sub
 ::available-games
 (fn []
   (fb-sub-user-id ["available"]))
 (fn [value]
   value))

;; (re-frame/reg-sub
;;  ::available
;;  :<- [::available-games]
;;  (fn [available-games [_ id]]
;;   ;;  (println "::available " id available-games)
;;    (let [value ((keyword id) available-games)]
;;      (re-frame/dispatch [::form-events/set-value! [:game-form :available id] value])
;;      value)))


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
 ::group-with-all
 (fn []
   (fb-sub-user-id ["group-with"]))
 (fn [value]
   value))

;; (re-frame/reg-sub
;;  ::group-with
;;  :<- [::group-with-all]
;;  (fn [value [_ id]]
;;    (let [val ((keyword id) value)]
;;      (re-frame/dispatch [::form-events/set-value! [:game-form :group-with id] val])
;;      val)))

(re-frame/reg-sub
 ::group-with
 :<- [::on-auth-value ["group-with"]]
 (fn [value [_ id]]
   (let [val ((keyword id) value)]
     (re-frame/dispatch [::form-events/set-value! [:game-form :group-with id] val])
     val)))
