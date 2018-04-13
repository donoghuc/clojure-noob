(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
(defn train
  []
  println "choo choo!")

;; Simple Math
(= (- 10 (* 2 3)) 4)

;; Intro to Functions
(= 8 ((fn add-five [x] (+ x 5)) 3))
(= 8 ((fn [x] (+ x 5)) 3))
(= 8 (#(+ % 5) 3))
(= 8 ((partial + 5) 3))

;; Double Down
(= (#(* % 2) 2) 4)

;; Hello World
(= (#(str "Hello, " % "!") "Dave") "Hello, Dave!")

;; Last Element: Restriction (last)
;;      solution
(fn [x] (if (> (count x) 0)
          (nth x (- (count x) 1))
          nil))
;;       test
(= ((fn [x] (if (> (count x) 0)
          (nth x (- (count x) 1))
          nil)) [1 2 3 4 5]) 5)

;; Regular Expressions
(= "ABC" (apply str (re-seq #"[A-Z]+" "bA1B3Ce ")))

;; Maximum Value: Restriction (max, max-key)
;;    solution
(fn [& acc] (reduce #(if (> %1 %2) %1 %2) acc))
#(reduce (fn [a b] (if (> a b) a b)) %&)
;;    test
(= ((fn [& acc] (reduce #(if (> %1 %2) %1 %2) acc)) 1 8 3 4) 8)

;; Interleaving two Seqs: Restriction (interleave)
;; recursive solution for fun (not allowed)
(defn recur-soln
  ([col-1 col-2] (recur-soln col-1 col-2 []))
  ([col-1 col-2 acc]
   (if (or (empty? col-1) (empty? col-2))
     acc
     (recur (rest col-1) (rest col-2) (conj acc (first col-1) (first col-2))))))
;; soln found
#(mapcat vector %1 %2)

;; simple recursion example
(defn recur-sum
  ([vals] (recur-sum vals 0))
  ([vals acc]
   (if (empty? vals)
     acc
     (recur-sum (rest vals) (+ (first vals) acc)))))

;; Simple recursion
((fn foo [x] (when (> x 0) (conj (foo (dec x)) x))) 5)

;; -> macro
(= (last (sort (rest (reverse [2 5 4 1 3 6]))))
   (-> [2 5 4 1 3 6] (reverse) (rest) (sort) (last))
   5)

;; ->> macro
(= (reduce + (map inc (take 3 (drop 2 [2 5 4 1 3 6]))))
   (->> [2 5 4 1 3 6] (drop 2) (take 3) (map inc) (reduce +))
   11)

;; Recurring Theme
(= [7 6 5 4 3]
  (loop [x 5
         result []]
    (if (> x 0)
      (recur (dec x) (conj result (+ 2 x)))
      result)))

;; nil key (get will return val or nil, therefore use contains?)
(true? (#(contains? %2 %1) :a {:a nil :b 2}))

;; A nil key #134 (also consider nil?)
(true? (#(= nil (get %2 %1 false)) :a {:a nil :b 2}))

;; for loops
(= '(1 5 9 13 17 21 25 29 33 37) (for [x (range 40)
            :when (= 1 (rem x 4))]
        x))

(= '(1 5 9 13 17 21 25 29 33 37) (for [x (iterate #(+ 4 %) 0)
            :let [z (inc x)]
            :while (< z 40)]
        z))

(= '(1 5 9 13 17 21 25 29 33 37) (for [[x y] (partition 2 (range 20))]
        (+ x y)))

;; map defaults
(= (#(zipmap %2 (repeat %1)) 0 [:a :b :c]) {:a 0 :b 0 :c 0})
#(into {} (map vector %2 (repeat %)))
#(into {}
       (for [k %2]
         [k %]))

;; implement nth Restriction: nth
(= (#(last (take (inc %2) %1)) '(4 5 6 7) 2) 6)
#(last (take (inc %2) %1))
#(first (drop %2 %1)) 

;; implement count Restriction: count
;; example
(= ((fn [x] (reduce + (map #(if % 1 1) x))) '(1 2 3 3 1)) 5)
;;soln
(fn [x] (reduce + (map #(if % 1 1) x)))
;; chouser soln
reduce #(do %2 (+ 1 %)) 0

;; reverse restrictions: revers rseq
;; function def not allowed (recursive soln)
(defn recur-rev
  ([col] (recur-rev col '()))
  ([col acc]
   (if (empty? col)
     acc
     (recur (rest col) (cons (first col) acc)))))

(recur-rev [1 2 3])

;; use into (cons will add to front)
into ()

(into () '(1 2 3))

;; palendrome detector
;; my soln
#(every? true? (map (fn [x] (= (first x) (last x))) (map vector % (reverse %))))
;; test
(#(every? true? (map (fn [x] (= (first x) (last x))) (map vector % (reverse %)))) "racecar")
;; chouser's soln (cast to seq for the string case)
#(= (seq %) (reverse %))

;; fibonacci (very interesting!)
;;http://blog.klipse.tech/clojurescript/2016/04/20/fibonacci.html
;; iterate
(def fib-seq-iterate
  (map first (iterate 
               (fn [[a b]] [b (+ a b)]) [0 1])))

(take 30 fib-seq-iterate)
;; lazy-cat
(def fib-seq-cat
  (lazy-cat [0 1] (map + (rest fib-seq-cat) fib-seq-cat)))

(take 30 fib-seq-cat)
;; lazy-seq
(def fib-seq-seq
  ((fn fib [a b] 
     (lazy-seq (cons a (fib b (+ a b)))))
   0 1))

(take 30 fib-seq-seq)
;; submitted solution
#(take % (map first (iterate (fn [[x y]] [y (+ x y)]) [1 1])))

(#(take % (map first (iterate (fn [[x y]] [y (+ x y)]) [1 1]))) 3)

;; amalloy/chouser
(fn f [a b n]
  (if (pos? n)
    (cons a
          (f b (+ a b) (dec n)))))
1 1 

((fn f [a b n]
  (if (pos? n)
    (cons a
          (f b (+ a b) (dec n)))))
1 1 3)

