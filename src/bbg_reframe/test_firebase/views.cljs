(ns bbg-reframe.test-firebase.views
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.test-firebase.subs :as subs]
   [bbg-reframe.test-firebase.events :as events]))

(defn game-div
  [id]
  (let [available (re-frame/subscribe [::subs/available id])
        in-box (re-frame/subscribe [::subs/group-with id])]
    ^{:key id}
    [:div
     (str id) " available: " (str @available)
    ;;  (if-not (nil? @available) (str @available) "null")
     " in box: "
     (if-not (nil? @in-box) (str @in-box) "null")
     [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/update-available id true])} "Make av"]
     [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/update-available id nil])} "Make non-av"]
     [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/update-group-with id (str (rand-int 999))])} "Random box"]
     [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/update-group-with id nil])} "Remove from box"]]))

(defn item-div
  [game-id collection-id]
  ^{:key game-id}
  [:li game-id " "
   [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/remove-game-from-collection (name game-id) (name collection-id)])} "X"]])


(defn collection-div
  [collection-id]
  (let [collection @(re-frame/subscribe [::subs/collection collection-id])]
    ^{:key collection-id}
    [:div
     [:h4 "Id: " collection-id " Name: " (:name collection)]
     ;; not sure why collection-id is a keyword
     [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/add-game-to-collection  (rand-int 1000) (name collection-id)])} "Add random item"]
     [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/delete-collection (name collection-id)])} "Delete collection"]
     [:ul (map #(item-div % collection-id) (:games collection))]]))

(defn games-div
  []
  [:div
   [:h2 "Games info"]
   [:div (doall (map #(game-div (str %)) (range 5)))]])

(defn collections-div
  []
  (let [collection-ids (re-frame/subscribe [::subs/collection-ids])]
    [:div
     [:h2 "Collections"]
     [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/new-collection (str "Collection-" (rand-int 1000))])} "New collection"]
     [:div (doall (map collection-div @collection-ids))]]))

(defn main-panel []
  (let [email (re-frame/subscribe [::subs/email])
        public-data (re-frame/subscribe [::subs/public-data])]
    [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
     [:h1.text-3xl.font-bold.mb-2.px-1 "Public data: "]
     [:div @public-data]
     [:h1.text-3xl.font-bold.mb-2.px-1 "User email: " @email]
     [:div [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/sign-in "dranidis@gmail.com" "password"])} "Sign-in as dranidis"]
      [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/sign-in "adranidisb@gmail.com" "password"])} "Sign-in as adranidisb"]
      [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/sign-up "dranidis@gmail.com" "password"])} "Sign up dranidis"]
      [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/sign-up "adranidisb@gmail.com" "password"])} "Sign up adranidisb"]
      [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/sign-out])} "Sign out"]]
     (when @email (games-div))
     (when @email (collections-div))]
     ;
    ))



