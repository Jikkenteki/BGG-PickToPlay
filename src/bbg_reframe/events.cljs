(ns bbg-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [bbg-reframe.model.localstorage :refer [set-item!]]
   [bbg-reframe.db :refer [default-db]]
   [clojure.spec.alpha :as s]
   [clojure.string :refer [trim]]
   [re-frame.loggers :refer [console]]
   [bbg-reframe.spec.db-spec :as db-spec]
   [bbg-reframe.model.sort-filter :refer [has-name? name-alpha?]]
   [re-frame-firebase-nine.fb-reframe :as fb-reframe]
   [re-frame.db :as db]
   [bbg-reframe.game-view.subs]
   [cljs.reader :refer [read-string]]
   [bbg-reframe.forms.events :as db-events]))

(defn check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (console :error (s/explain-str  a-spec db))
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

;; now we create an interceptor using `after`
(def check-spec-interceptor (re-frame/after (partial check-and-throw ::db-spec/db)))


(re-frame/reg-event-db
 ::initialize-db
 [check-spec-interceptor]
 (fn-traced
  [_ _]
  default-db))

;; (re-frame/reg-event-db
;;  ::field
;;  (fn-traced[db [_ field e]]
;;    (let [_ (println e field)
;;          new-fields
;;          (if (some #(= field (:name %)) (:fields db))
;;            (filter #(not= field %) (:fields db))
;;            (conj (:fields db) field))]
;;      (assoc db :fields new-fields))))



(re-frame/reg-event-fx
 ::update-form
 [check-spec-interceptor]
 (fn-traced [{:keys [db]} [_ id val]]
            (set-item! "bgg-ui-settings" (assoc (:form db) id val))
            {:db (assoc-in db [:form id] val)
             :dispatch [:bbg-reframe.network-events/update-result]}))

(re-frame/reg-event-fx
 ::update-user
 [check-spec-interceptor]
 (fn-traced [{:keys [db]} [_ val]]
            (let [user (trim val)
                  _ (set-item! "bgg-user" user)]
              {:db (assoc db :user user)})))

(re-frame/reg-event-fx
 ::search-name-with-substring
 [check-spec-interceptor]
 (fn-traced [{:keys [db]} [_ val]]
            (let [filtered
                  (if (> (count val) 1)
                    (->> (vals (:games db))
                         (filter (has-name? val))
                         (sort name-alpha?))
                    [])]
              {:db (assoc db :substring val :search-results filtered)})))

(re-frame/reg-event-fx
 ::update-games
 [check-spec-interceptor]
 (fn-traced [{:keys [db]} [_ val]]
            {:db (assoc db :games val)}))

(re-frame/reg-event-fx
 ::update-ui-settings
 [check-spec-interceptor]
 (fn-traced [{:keys [db]} [_ val]]
            {:db (assoc db :form val)}))

(re-frame/reg-event-db
 ::set-open-tab
 [check-spec-interceptor]
 (fn-traced [db [_ tab]]
            (assoc-in db [:ui :open-tab] (if (= (get-in db [:ui :open-tab]) tab)
                                           nil
                                           tab))))

;; routing
(comment
  ;; navigates to the route specified by the keyword
  (re-frame/dispatch [::navigate :home])
  (re-frame/dispatch [::navigate :fb]))

(re-frame/reg-event-fx
 ::navigate
 (fn-traced [_ [_ handler]]
            {:navigate handler}))

;; (re-frame/reg-event-fx
;;  ::set-active-panel
;;  (fn-traced [{:keys [db]} [_ active-panel]]
;;             {:db (assoc db :active-panel active-panel)}))

(re-frame/reg-event-fx
 ::set-route
 (fn-traced [{:keys [db]} [_ route]]
            {:db (assoc db :route route)}))


;; fb
(re-frame/reg-event-fx
 ::fb-set
 (fn-traced [_ [_ path value]]
            {::fb-reframe/firebase-set {:path (into ["users" (fb-reframe/get-current-user-uid)] path)
                                        :data value
                                        :success #(println "Success")}}))

(comment

  (fb-reframe/get-current-user-uid)
  (def db @re-frame.db/app-db)
  (def first-game (-> (:games db)
                      (vals)
                      (first)))
  first-game
  (def first-game-value {(:id first-game) first-game})
  first-game-value
  (fb-reframe/get-current-user-uid)
  (re-frame/dispatch [::fb-set ["cached-games"] (str first-game-value)])

  (re-frame/dispatch [::fb-set ["test"] "value"])

  @(re-frame/subscribe [::cached-games])
  (def cached @(re-frame/subscribe [::bbg-reframe.game-view.subs/on-auth-value ["cached-games"]]))
  cached

  (read-string cached)

  (def allgames @(re-frame/subscribe [::bbg-reframe.forms.events/get-value [:games]]))
  allgames

  (re-frame/dispatch [::bbg-reframe.forms.events/set-value! [:games] (read-string cached)])



  {})