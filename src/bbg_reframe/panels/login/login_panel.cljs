(ns bbg-reframe.panels.login.login-panel
  (:require [bbg-reframe.firebase.firebase-events :as firebase-events]
            [bbg-reframe.forms.forms :refer [input-element]]
            [bbg-reframe.forms.subs :as form-subs]
            [bbg-reframe.panels.home.components.fetch-collection-comp :refer [fetch-collection-comp]]
            [bbg-reframe.panels.login.login-events :as login-events]
            [bbg-reframe.panels.login.login-subs :as login-subs]
            [clojure.string :refer [trim]]
            [re-frame.core :as re-frame]))

(defn save-games
  []
  (re-frame/dispatch [::firebase-events/fb-save-games]))

(defn login-comp
  []
  (let [email @(re-frame/subscribe [::form-subs/get-value [:login-form :email]])
        password @(re-frame/subscribe [::form-subs/get-value [:login-form :password]])]
    [:div.gap-2.flex-col.flex
     [:h2 "PickToPlay account"]
     [:h3 (if
           (nil? @(re-frame/subscribe [::login-subs/email]))
            "Not signed in"
            (str "Signed in as: " @(re-frame/subscribe [::login-subs/email])))]
     (if
      (nil? @(re-frame/subscribe [::login-subs/email]))
       [:<>
        [input-element {:class "input-box min-w-0 grow h-full"
                        :type :text
                        :placeholder "email"
                        :path [:login-form :email]
                        :post-fn trim}]
        [input-element {:class "input-box min-w-0 grow h-full"
                        :type :password
                        :placeholder "password"
                        :path [:login-form :password]}]
        [:button.button
         {:on-click #(re-frame/dispatch [::login-events/sign-in email password])} "Sign in"]
        [:button.button
         {:on-click #(re-frame/dispatch [::login-events/sign-up email password])} "Sign up"]]
       [:<>
        [:button.button
         {:disabled (nil? @(re-frame/subscribe [::login-subs/email]))
          :on-click #(re-frame/dispatch
                      [::login-events/sign-out])} "Sign out"]
        [:button.button
         {:disabled (nil? @(re-frame/subscribe [::login-subs/email])) :on-click save-games} "Save games to account"]
        [:button.button
         {:disabled (nil? @(re-frame/subscribe [::login-subs/email])) :on-click #(re-frame/dispatch [::firebase-events/fetch-games])} "Fetch games from account"]])]))

(defn login-view-panel
  []
  [:div.p-4.max-w-xl.mx-auto.flex.flex-col.h-full.gap-6.bg-stone-800.text-neutral-200
   [:h1 "User"]
   [login-comp]
   [fetch-collection-comp]])