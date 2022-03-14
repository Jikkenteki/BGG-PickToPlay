(ns bbg-reframe.routes
  (:require
   [bidi.bidi :as bidi]
   [pushy.core :as pushy]
   [re-frame.core :as re-frame]
   [bbg-reframe.events :as events]))

(defmulti panels identity)
(defmethod panels :default [] [:div "No panel found for this route."])

(def routes
  (atom
   ["/" {""   :home
         "fb" :fb}]))

(defn parse
  "Parse a url string and return a map with the :handler keyword according to
   the @routes table."
  [url]
  (bidi/match-route @routes url))

(defn url-for
  [& args]
  (apply bidi/path-for (into [@routes] args)))

(defn dispatch
  [route]
  (let [panel (keyword (str (name (:handler route)) "-panel"))]
    (re-frame/dispatch [::events/set-active-panel panel])))

(defonce history
  (pushy/pushy dispatch parse))

(defn navigate!
  [handler]
  (pushy/set-token! history (url-for handler)))

(defn start!
  []
  (pushy/start! history))

(re-frame/reg-fx
 :navigate
 (fn [handler]
   (navigate! handler)))



(comment
  history
  (def handler :home)
  (url-for handler) ;; "/"


  (parse "/") ;; {:handler :home}
  (def route (parse "/"))
  (:handler route)  ;; :home
  (keyword (str (name (:handler route)) "-panel")) ;; :home-panel


  (navigate! :home)

  ;
  )