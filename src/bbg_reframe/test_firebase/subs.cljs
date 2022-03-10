(ns bbg-reframe.test-firebase.subs
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.test-firebase.firebase.firebase-auth :refer [get-current-user-uid]]
   [bbg-reframe.test-firebase.firebase.fb-reframe :as fb-reframe]))

(defn fb-sub-user-id
  [path]
  (re-frame/subscribe [::fb-reframe/on-value (concat ["users" (get-current-user-uid)] path)]))

(defn fb-sub-root
  [path]
  (re-frame/subscribe [::fb-reframe/on-value  path]))


(re-frame/reg-sub
 ::email
 (fn [db]
   (:bbg-reframe.test-firebase.events/email db)))

;;
;; collections
;;
(re-frame/reg-sub
 ::collections
 (fn []
   (fb-sub-user-id ["collections"]))
 (fn [collections]
  ;;  (println "collections" collections)
   collections))

(re-frame/reg-sub
 ::collection-ids
 :<- [::collections]
 (fn [collections]
   (keys collections)))

;; get the collection with id
;; game-ids are in the keys (e.g. {132: true, 124: true})
(re-frame/reg-sub
 ::collection
 :<- [::collections]
 (fn [collections [_ id]]
   (let [collection (id collections)
         games (keys (:games collection))]
     (assoc collection :games games))))


(re-frame/reg-sub
 ::public-data
 (fn []
   (fb-sub-root ["public"]))
 (fn [value]
   value))


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
   ((keyword id) available-games)))

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
   ((keyword id) value)))

