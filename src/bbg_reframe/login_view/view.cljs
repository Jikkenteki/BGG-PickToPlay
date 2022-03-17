(ns bbg-reframe.login-view.view
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.forms.forms :refer [input]]
            [bbg-reframe.forms.form-subs :as subs]
            [bbg-reframe.login-view.events :as events]))

(defn login-view-panel
  []
  [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
   [:h1 "Login"]

   (let [form @(re-frame/subscribe [::subs/get-value [:login-form]])]
     [:div
      [input "email" :text [:login-form :email]]
      [input "password" :password [:login-form :password]]
      [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/sign-in (:email form) (:password form)])} "Sign in"]
      [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/sign-up (:email form) (:password form)])} "Sign up"]
      [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/sign-out])} "Sign out"]])])