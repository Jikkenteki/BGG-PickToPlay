(ns bbg-reframe.model.collection)

(defn make-collection
  ([name]
   {:name name})
  ([id name]
   {:id id :name name}))

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn get-collections
  [collections]
  (reduce-kv (fn [m k v] (conj m (make-collection k (:name v)))) [] collections))

(defn get-collection-names [collections]
  (map :name (get-collections collections)))

;; (defn get-collection-names [collections]
;;   (if (nil? collections)
;;     []
;;     (reduce-kv (fn [m _ v] (conj m (:name v))) [] collections)))

(defn collection-name-exists?
  [collections name]
  (in? (get-collection-names collections) name))

(comment

  (def collections {:-NTK-cZZcAJzjdrG6ifV {:name "ac"}
                    :af {:name "ab"}})

  (get-collections collections)
  (get-collection-names collections)

  ;
  )