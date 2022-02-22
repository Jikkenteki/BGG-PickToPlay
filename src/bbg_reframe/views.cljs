(ns bbg-reframe.views
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]
   [bbg-reframe.model.db :refer [all-fields xml-to-clj]]
   [bbg-reframe.model.sort-filter :refer [rating-for-number-of-players sorting-fun]]
   [goog.string :as gstring]
   [goog.string.format]
   ["sax" :as sax]
   [bbg-reframe.model.http :refer [get-xml]]
   [cljs.pprint :refer [pprint]]
   [bbg-reframe.model.tmp :refer [long-xml]]))

; required for tubax to work
(js/goog.exportSymbol "sax" sax)

(def xml-atom (atom "<a>A</a>"))

(defn field-div
  [fields field]
  ^{:key (random-uuid)}
  [:label [:input {:type "checkbox"
                   :name "field"
                   :value field
                   :checked (some #(= field %) fields)
                   :on-change #(re-frame/dispatch
                                [::events/field field (-> % .-target .-value)])}]
   field])

(defn fields-div
  [fields]
  [:div
   [:form
    [:fieldset
     [:legend " Show fields"]
     (map #(field-div fields %) all-fields)]]])

(defn game-div
  [game fields players]
  (let [playability (gstring/format "%.2f" (rating-for-number-of-players
                                            game players))]
    ^{:key (random-uuid)}
    [:tr
     (map (fn [field]
            ^{:key (random-uuid)}
            [:td (if (= field "playability")
                   playability
                   (game (keyword field)))])
          fields)]))

(defn result-div
  [result fields players]
  (let [fields-sorted (filter (fn [f] (some #(= f %) fields)) all-fields)]
    [:div "Games "
     [:table
      [:tbody
       [:tr
        (map (fn [field]
               ^{:key (random-uuid)}
               [:th field])
             fields-sorted)]
       (map
        (fn [game]
          (game-div game fields-sorted players))
        result)]]]))

(defn select
  [id label options]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div
     [:label label]
     [:select {:value @value
               :on-change #(re-frame/dispatch [::events/update-form
                                               id
                                               (-> % .-target .-value)])}
      (map (fn [o] [:option {:key o :value o} o]) options)]]))

(defn slider
  [id label min max step]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div
     [:label label " " @value]
     [:br]
     [:input {:type "range" :min min :max max :step step :value @value :id id
              :onChange #(re-frame/dispatch [::events/update-form
                                             id
                                             (-> % .-target .-value)])}]]))


(defn main-panel []
  (let [_ (get-xml xml-atom "2651")
                   result (re-frame/subscribe [::subs/result])
        fields (re-frame/subscribe [::subs/fields])
        players (re-frame/subscribe [::subs/form :players])]
    [:div
     [:h1
      "BGG "]
     [:button {:on-click #(re-frame/dispatch [::events/handler-with-http])} "Get"]
     (fields-div @fields)
     (select :sort-id "Sort by " (map name (keys sorting-fun)))
     (slider :take "Take" 1 25 1)
     (slider :higher-than "Rating higher than" 0 10 0.1)
     (slider :players "For number of players" 1 10 1)
     (slider :threshold "Playability threshold" 0 0.95 0.05)
     (slider :time-limit "Time limit" 0 500 10)
     (result-div @result @fields @players)
     [:pre
      "XML 2651"

;; (clojure.string/replace "sdfsdafsa [ fas a  ] faf afa" #"[\[\]]" "")


      ;; (with-out-str (pprint (xml-to-clj "<boardgames termsofuse=\"https://boardgamegeek.com/xmlapi/termsofuse\">\n\t\t\t\t\t<boardgame objectid=\"2651\">\n\t\t\t<yearpublished>2004</yearpublished>\n\t\t\t<minplayers>2</minplayers>\n\t\t\t<maxplayers>6</maxplayers>\n\t\t\t<playingtime>120</playingtime>\n\t\t\t<minplaytime>120</minplaytime>\n\t\t\t<maxplaytime>120</maxplaytime>\n\t\t\t<age>12</age>\n\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Alta Tensão</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Alta Tensao</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Alta Tensión</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Alta Tensión (Reenergizado)</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Alta Tensione</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Funkenschlag</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Funkenschlag (Recharged Version)</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Haute Tension</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Hoogspanning</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Hoogspanning (Recharged Versie)</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Megawatts</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Nagyfeszültség</name>\n\t\t\t\t\t\t\t<name primary=\"true\" sortindex=\"1\">Power Grid</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Power Grid (Recharged Version)</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Power Grid (Versao Energizada)</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Reţeaua Energetică</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Vysoké Napětí</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Wysokie Napięcie</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Wysokie Napięcie: Doładowana Wersja</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">Энергосеть</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">เกมโรงไฟฟ้า</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">電力会社</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">電力会社 充電完了！</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">電力公司</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">파워그리드</name>\n\t\t\t\t\t\t\t<name  sortindex=\"1\">파워그리드 재충전</name>\n\t\t\t\t\t\t\t\t\t\n\t\t\t</boardgame>\n\t</boardgames>\n")))
      ;; (with-out-str 


      (with-out-str
        (pprint
         (xml-to-clj  @xml-atom  false)))

      

      ;; (pprint
      ;;  (xml-to-clj
      ;;   (get-xml xml-atom "2651") false))
        ;; )

      ;; (xml-test (get-xml))
      ]]))
