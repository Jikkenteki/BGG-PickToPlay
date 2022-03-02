 (ns bbg-reframe.model.tag-helpers)

(defn has-tag?
  [tag-name]
  (fn [{:keys [tag]}]
    (= tag tag-name)))

(defn has-attr-with-value?
  [attr value]
  (fn [x]
    = (get-in x [:attrs attr]) value))

  ;; mutual recursion
(declare find-element-with-tag)
(defn find-tag-list
  [s tag]
  (->> (map #(find-element-with-tag tag %) s)
       flatten
       (drop-while nil?)
       first))

(defn find-element-with-tag [tag s]
  (if ((has-tag? tag) s)
    s
    (if (:content s)
      (find-tag-list (:content s) tag)
      nil)))
