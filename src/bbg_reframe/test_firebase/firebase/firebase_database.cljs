(ns bbg-reframe.test-firebase.firebase.firebase-database
  (:require ["firebase/database" :as fdb]
            [clojure.string :as string]
            [re-frame.loggers :refer [console]]
            [bbg-reframe.test-firebase.firebase.firebase-app :refer [init-app]]
            [bbg-reframe.test-firebase.firebase.firebase-auth :refer [get-current-user-uid]]))

(defn- get-db
  []
  (fdb/getDatabase (init-app)))

(defn- db-ref
  [db path]
  (fdb/ref db (string/join "/" path)))

(defn- fb-set!
  ([ref data] (fb-set! ref data (fn []) (fn [_])))
  ([ref data then-callback catch-callback]
   (-> (fdb/set
        ref (clj->js data))
       (.then then-callback)
       (.catch catch-callback))))

;;
;; public functions
;;
(defn set-value!
  ([path data] (set-value! path data (fn []) (fn [_])))
  ([path data then-callback catch-callback]
   (fb-set! (db-ref (get-db) path) data then-callback catch-callback)))


;; Use the push () method to append data to a list in multiuser applications. 
;; The push () method generates a unique key every time a new child is added to the specified Firebase reference.
;; The .key property of a push () reference contains the auto-generated key.
;; (defn- push-ref
;;   [path]
;;   (fdb/push (db-ref (get-db) path)))

(defn push-value!
  ([path data] (push-value! path data (fn []) (fn [_])))
  ([path data then-callback catch-callback]
   (let [ref (fdb/push (db-ref (get-db) path))]
     (fb-set! ref data then-callback catch-callback)
     (.-key ref))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn on-value
  "Default only-once is false"
  ([path callback] (on-value path callback false))
  ([path callback only-once?]
   (fdb/onValue (db-ref (get-db) path)
                (fn [snap-shot]
               ;; ^js https://shadow-cljs.github.io/docs/UsersGuide.html#infer-externs
                  (callback (js->clj (.val ^js snap-shot) :keywordize-keys true)))
                #js {:onlyOnce only-once?})))

(defn off
  "Detach listeners"
  ([path] (fdb/off (db-ref (get-db) path)))
  ([path listener]
   (fdb/off (db-ref (get-db) path) listener)))



(defn default-set-success-callback
  [])

(defn default-set-error-callback
  [error]
  (console :error (js->clj error)))

(comment
  (set-value! ["users" (get-current-user-uid) "games" "2" "available"] false
              #(println "SUCCESS") #(println "ERRROR" (js->clj %)))

  ;; make game available
  ;; (set-value! ["users" (get-current-user-uid) "games" "0"] {:available true}
  ;;             #(println "SUCCESS") #(println "ERRROR" (js->clj %)))


  ;; (re-frame/dispatch [::events/save {:a 1 :b 2 :name "Dimitris" :m [1 2 3]}])

  (push-value! ["users" (get-current-user-uid) "collections"] "test3")
      ;
  )





