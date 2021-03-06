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

;; get the caps
#(apply str (re-seq #"[A-Z]+" %))

(#(apply str (re-seq #"[A-Z]+" %)) "abcDEfgH")

;; duplicate sequence
;; my solution
#(mapcat vector % %)

(#(mapcat vector % %) [1 2 3])

;; chouser
#(interleave % %)

;; cgrand
#(for [x % y [x x]] y)

;; range Restrictions: range
#(take (Math/abs (- %1 %2)) (iterate inc %1))
(= (#(take (Math/abs (- %1 %2)) (iterate inc %1)) 1 4) '(1 2 3))

;; compress a sequence
;; my recursive solution
(defn recur-sol
  ([col] (recur-sol col []))
  ([col acc]
   (if (empty? col)
     acc
     (recur (rest col) (if-not (= (first col) (second col))
                         (conj acc (first col))
                         acc)))))

(recur-sol [1 1 2 3 1 1 1 2])
;; chouser
#(map first (partition-by identity %))
;; amalloy
#(map last (partition-by max %))

;; factorial fun
;; my solution using reduce
#(reduce * (range 1 (inc %)))

(#(reduce * (range 1 (inc %))) 3)

;; chouser/amalloy/cgrand
#(apply * (range 1 (inc %)))

;; implement flatten Restriction: flatten
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Try to solve with recursion. FAIL
(defn recur-flat
  ([col] (recur-flat col '()))
  ([col acc]
   (if (empty? col)
     acc
     (if-not (sequential? (first col))
       (recur-flat (rest col) (cons (first col) acc))
       (recur-flat (first col) acc)))))

(recur-flat [1 2 [2 3] 4])

(recur-flat [1 2 3 [1 2 3] 4 5 [1 2]])
;; loose the rest of the seq when you go down a level
(defn recur-flat
  ([col] (recur-flat col 0 []))
  ([col depth acc]
   (if (and (empty? col) (zero? depth))
     acc
     (if-not (sequential? (first col))
       (recur-flat (rest col) (rest full) (conj acc (first col)))
       (recur-flat (first col) full acc)))))

(recur-flat [1 2 [2 3] 4])

(recur-flat [1 2 3 [1 2 3] 4 5 [1 2]])   

;; find solution on clojuredocs.org/clojure.core/tree-seq 
(tree-seq seq? identity [1 2 3 [1 2]])

(def t '((1 2 (3)) (4)))

(tree-seq sequential? seq t)

(defn flatten [x]
  (filter (complement sequential?)
          (rest (tree-seq sequential? seq x))))

(flatten t)
;; solution
﻿#(filter (complement sequential?) (tree-seq sequential? identity %))

(#(filter (complement sequential?) (tree-seq sequential? identity %)) [1 2 3 [1 2 3] 4 5 [1 2]])

;;chouser
(fn f [x] (mapcat #(if (coll? %) (f %) [%]) x))


;; cgrand/amalloy
(fn f [x] (if (coll? x) (mapcat f x) [x]))

((fn f [x] (if (sequential? x) (mapcat f x) [x])) [1 2 3 4])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;Replicate a sequence #33
;; first crack, didnt need take because repeat can take number of repeats arg
(fn [col rep] (mapcat #(take rep (repeat %)) col))

((fn [col rep] (mapcat #(take rep (repeat %)) col)) [1 2 3] 3)
;; chouser
#(mapcat (partial repeat %2) %)
;;cgrand
#(for [x % y (repeat %2 x)] y)

;; intro to iterate
(take 5 (iterate #(+ 3 %) 1))
;; (1 4 7 10 13)

;; Interpose #40 Restriction: interpose
#(drop-last (mapcat vector %2 (repeat %1)))

(#(drop-last (mapcat vector %2 (repeat %1))) 0 [1 2 3])

;;chouser
#(rest (apply concat (for [i %2] [% i])))

;; cgrand
#(next (mapcat (fn [x] [% x]) %2))

;;pack a sequence #31 
#(partition-by identity %)

(#(partition-by identity %) [1 1 2 1 1 1 3 3])

;;cgrand
partition-by #(do %)

;; drop every nth item #41
(fn [col n] (mapcat #(if (= n (count %)) (drop-last %) %) (partition-all n col)))

((fn [col n] (mapcat #(if (= n (count %)) (drop-last %) %) (partition-all n col))) [1 2 3 4 5 6 7 8] 3)

;;cgrand
#(keep-indexed (fn [i x] (when (< 0 (mod (+ 1 i) %2)) x)) %)

;; Split a sequence #49
#(vector (into [] (take %1 %2)) (into [] (drop %1 %2)))
(#(vector (into [] (take %1 %2)) (into [] (drop %1 %2))) 3 [1 2 3 4])

;;chouser
(juxt take drop)

;; Advanced destructuring #51
(= [1 2 [3 4 5] [1 2 3 4 5]] (let [[a b & c :as d] (range 1 6)] [a b c d]))

;; half-truth #83 (true if some but not all, false if none)
(fn [& bools]
  (if (and (not (every? true? bools)) (some true? bools))
    true
    false))

((fn [& bools]
  (if (and (not (every? true? bools)) (some true? bools))
    true
    false)) true true false)

;; chouser (genius)
#(= 2 (count (set %&)))

(#(= 2 (count (set %&))) true true false)

;;amalloy (durrr)
(not= true true false)

;; slack
(def header-raw [0 0 0 1 0 11 42 70 0 37 -78 80 0 0 47 47 0 0 0 0 90 -44 -36 -101])

;;tlehman
(let [x (doto (java.nio.ByteBuffer/wrap (byte-array 24 header-raw))
          (.getInt)
          (.getInt)
          (.getInt)
          (.getInt))]
 (.getLong x))

;; cas
(let [x (java.nio.ByteBuffer/wrap (byte-array 24 header-raw))]
  (dotimes [i 4] (.getInt x))
  (.getLong x))

;; Justin Smith
(let  [bb  (java.nio.ByteBuffer/wrap  (byte-array 24 header-raw))  
      consume #(.getInt bb)                                                    
      skip #(dotimes [i %] (consume))                                          
      seq-number (consume)                                                            
      _ (skip 1)                                                              
      comp-size  (consume)                                                             
      _ (skip 2)                                                              
      full-size  (consume)]                                                            
 [seq-number comp-size full-size]) 

;; justin smith
(let [bb (java.nio.ByteBuffer/wrap (byte-array 24 header-raw))                         
     consume #(.getInt bb)             
     ;; int-template describes how to keep or discard a sequence of ints      
     int-template [[true 1] [false 1] [true 1] [false 2] [true 1]]]                    
 ;; this is my typical mapcat usage - to simultaniously filter and concatenate         
 (mapcat (fn [[use? n]]                                                        
           (if use?                                                                    
             ;; doall is needed here because otherwise laziness causes an      
             ;; incorrect result                                              
             (doall (repeatedly n consume)) ;; included in the result          
             (dotimes [_ n] (consume)))) ;; returns nil - no output                    
         int-template))

;; Justin reduce
(let [bb (java.nio.ByteBuffer/wrap (byte-array 24 header-raw))         
      consumption-template [true false true false false true]]                         
 (reduce (fn [acc hold?]                                               
           (let [i (.getInt bb)]                                                       
             (if hold?                                                        
               (conj acc i)                                                    
               acc)))                                                          
         []                      
         consumption-template))



;; Map Construction #61
#(into {} (map vector %1 %2))

(#(into {} (map vector %1 %2)) [1 2 3] [4 5 6])

;; greatest common denominator #66
(fn [a b]
  (cond
   (zero? a) b
   (zero? b) a
   :else (recur (rem (max a b) (min a b)) (min a b))))

((fn [a b]
  (cond
   (zero? a) b
   (zero? b) a
   :else (recur (rem (max a b) (min a b)) (min a b)))) 192 270)

;; chouser
#(if (ratio? (/ % %2)) (/ % (numerator (/ % %2))) (min % %2))

(#(if (ratio? (/ % %2)) (/ % (numerator (/ % %2))) (min % %2)) 192 270)

;; Set Intersection #81
(fn [col-1 col-2]
  (into #{}
        (remove nil?
                (map #(if (contains? col-1 %) %) col-2))))

((fn [col-1 col-2]
  (into #{}
        (remove nil?
                (map #(if (contains? col-1 %) %) col-2)))) #{1 2 3} #{})

;;chouser
#(set (filter % %2))
;;cgrand
(comp set filter)
;; noisesmith
(fn [s & ss]
  (letfn [(inter [a b]
            (reduce (fn [c el]
                      (if (contains? b el)
                        (conj c el)
                         c))
                         #{}
                         a))]
    (reduce inter s ss)))

;;Simple closures #107
(fn [power] #(reduce * (repeat power %)))

(((fn [power] #(reduce * (repeat power %))) 2) 16)

;;cgrand
#(fn [x] (Math/pow x %))

;; noisesmith
(fn [n]
  (fn [x]
    (apply * (repeat n x))))

;;Re-implement Iterate #62
(defn iter [f x]
  (lazy-seq
   (cons x (iter f (f x)))))

(= (take 5 (iter #(* 2 %) 1)) [1 2 4 8 16])

;;without defn
(fn iter [f x]
  (lazy-seq
   (cons x (iter f (f x)))))

;; amalloy
(fn [f x]
  (reductions (fn [a _] (f a))
              x
             (range)))

;; Comparisons #166
(fn [comp a b]
  (cond
   (comp a b) :lt
   (comp b a) :gt
   :else :eq))

((fn [comp a b]
  (cond
   (comp a b) :lt
   (comp b a) :gt
   :else :eq)) < 5 5)

;; Cartesiann Product #90
#(into #{} (for [a %1 b %2] [a b]))

(#(into #{} (for [a %1 b %2] [a b])) #{1 2 3} #{4 5})

;;Product Digits #99
;;my sumbission
(fn [a b]
   ((fn recur-soln
      ([value] (recur-soln value '()))
      ([value acc] (let [x (/ value 10)
                         f #(* 10 (- (/ % 10) (int (/ % 10))))]
                     (if-not (>= x 1)
                       (cons (f value) acc)
                       (recur (int x) (cons (f value) acc)))))) (* a b)))
;; test
((fn [a b]
   ((fn recur-soln
      ([value] (recur-soln value '()))
      ([value acc] (let [x (/ value 10)
                         f #(* 10 (- (/ % 10) (int (/ % 10))))]
                     (if-not (>= x 1)
                       (cons (f value) acc)
                       (recur (int x) (cons (f value) acc)))))) (* a b))) 8 9)

;;chouser
#(for [c (str (* % %2))] (- (int c) 48))

;;cgrand
#(map (zipmap "0123456789" (range 10)) (str (apply * %&)))

;;amalloy
#(map (comp read-string str)
      (str (* % %2)))

;;noisesmith
(fn digits [a b]
  (->> (* a b)
       (repeat 2)
       (iterate (fn chop [[_ pool]]
                  [(rem pool 10) (quot pool 10)]))
       (take-while #(not (every? zero? %)))
       (rest)
       (map first)
       (reverse)))

;; Group a sequence #63 
;; recursive solution
(defn tst [expr col]
  ((fn [proc acc]
     (if (empty? proc)
       acc
       (let [i (first proc)]
         (if (contains? acc (first i))
           (recur (rest proc) (update acc (first i) conj (last i)))
           (recur (rest proc) (assoc acc (first i) (vector (last i))))))))
   (map vector (map expr col) col) {}))

(tst #(> % 5) [1 3 6 8])

;; solution 1
(fn [expr col]
  (apply merge-with concat (map (fn [a b] {a [b]}) (map expr col) col)))
;; solution 1 test
((fn [expr col]
  (apply merge-with concat (map (fn [a b] {a [b]}) (map expr col) ))) #(> % 5) [1 3 6 8])

;; chouser
#(reduce
  (fn [m x]
    (assoc m (% x) (conj (m (% x) []) x)))
  {} %2)

;;cgrand
#(reduce
  (fn [m x]
    (assoc m (% x) (conj (m (% x) []) x)))
  {} %2)

;;cgrand
#(apply merge-with into (map (fn [x] {(% x) [x]}) %2))

;; amalloy
#(apply merge-with into
        (for [x %2]
          {(% x) [x]}))

;;noisesmith
;;#(reduce (fn [m el]
;;           (let [lookup (%1 el)
;;                 existing (get m lookup [])
;;                 updated (conj existing el)]
;;             (assoc m lookup updated)))
;;         {}
;;         %2)

#(reduce (fn [m el]
           (update-in m [(%1 el)] (fnil conj []) el))
         {}
         %2)

;;symetric difference # 88
;;casadilla
#(into #{} (remove (clojure.set/intersection %1 %2) (clojure.set/union %1 %2)))

(= ( #(into #{} (remove (clojure.set/intersection %1 %2) (clojure.set/union %1 %2))) #{1 2 3 4 5 6} #{1 3 5 7}) #{2 4 6 7})

;;gcrand amalloy
#(set (concat (remove % %2) (remove %2 %)))

;; juxt example
(def ex-map {:h [1 2]
             :e [3 4]
             :l [5 6]
             :p [7 8]
             })

(into [] ((juxt :h :e :l :p) ex-map))


;; dot product #143

;; submission
#(reduce + (map * %1 %2))

;; could use apply
#(apply + (map * %1 %2))
;; test
(= 0 (#(reduce + (map * %1 %2)) [0 1 0] [1 0 0]))

;;binary to decimal # 122
;;casadilla
(fn [binary]
  (reduce + (map #(if (= %1 \1) %2 0) (reverse binary) (iterate #(* 2 %) 1))))
;;test
(= 65535 ((fn [bin] (reduce +
                  (map #(if (= %1 \1) %2 0) bin)
                  (iterate #(* 2 %) 1))) "1111111111111111"))

;;chouser
#(read-string (str "2r" %))

;;cgrand
reduce #(+ % % ({\0 0} %2 1)) 0

;;noisesmith
#(first
  (reduce
   (fn [[total mult] item]
     [(bit-or total (bit-shift-left (get {\0 0 \1 1} item) mult))
      (inc mult)])
   [0 0]
   (reverse %)))

;; infix calculator
;;casadilla
(fn [& stack]
  ((fn recur-calc
     [col acc]
     (if (= 2 (count col))
       ((first col) acc (last col))
       (recur (drop 1 (rest col)) ((first col) acc (nth col 1)))))
   (rest stack) (first stack)))

;; test
((fn [& stack]
  ((fn recur-calc
     [col acc]
     (if (= 2 (count col))
       ((first col) acc (last col))
       (recur (drop 1 (rest col)) ((first col) acc (nth col 1))))) (rest stack) (first stack))) 20 / 2 + 2 + 4 + 8 - 6 - 10 * 9)

;;chouser
(fn f
  ([a] a)
  ([a b c & m]
   (apply f (b a c) m)))

;;cgrand
(fn [x & xs]
  (reduce (fn [x [f y]] (f x y)) x
    (partition 2 xs)))

;;amalloy
(fn c [x f y & r]
  ((if r
     #(apply c % r) +)
   (f x y)))

;;noisesmith
(fn infixr
  [& steps]
  (loop [[a op b & remaining] steps]
    (let [step (op a b)]
      (if (empty? remaining)
        step
        (recur (cons step remaining))))))


;; Indexing Sequences #157
;;casadilla
#(map vector % (range))
;;test
(#(map vector % (range)) [:a :b :c])

;;cgrand
map-indexed #(list %2 %)

;;noisesmith
map-indexed
 (comp reverse list)

;;Pascal triangle #97
;;use algo C(line, i)   = line! / ( (line-i)! * i! ) 
;;https://www.geeksforgeeks.org/pascal-triangle/

;; solution used
(fn [line]
  (for [x (range line)]
    (/ (#(if (<= % 1) 1 (reduce * (range 1 (inc %)))) (- line 1)) (* (#(if (<= % 1) 1 (reduce * (range 1 (inc %)))) (- (- line 1) x)) (#(if (<= % 1) 1 (reduce * (range 1 (inc %)))) x)))))

;; using let
(fn [line]
  (let [fa #(if (<= % 1) 1 (reduce * (range 1 (inc %))))
        index0->1 (- line 1)]
    (for [x (range line)]
      (/ (fa index0->1) (* (fa (- index0->1 x)) (fa x))))))
;; test
((fn [line]
  (let [fa #(if (<= % 1) 1 (reduce * (range 1 (inc %))))
        index0->1 (- line 1)]
    (for [x (range line)]
      (/ (fa index0->1) (* (fa (- index0->1 x)) (fa x)))))) 5)

;; chouser
(fn p [x]
  (if (= x 1)
    [1]
    `[1 ~@(map + (p (- x 1)) (next (p (- x 1)))) 1]))
;; cgrand
(fn [n]
  (nth (iterate #(concat [1] (map + % (next %)) [1]) [1]) (dec n)))
;; amalloy
(fn [n]
  (-> (iterate (fn [row]
                 (map + `(0 ~@row) `(~@row 0)))
               [1])
      (nth (dec n))))
;; noisesmith
#(letfn [(row [prev]
           (concat [1] (map (partial apply +) (partition 2 1 prev)) [1]))]
   (nth (iterate row [1]) (dec %)))


;;Re-Implement Map # 118

;; recursive! third unit test blows stack :(
(fn recur-map
  ([f col] (recur-map f col []))
  ([f col acc]
   (if (empty? col)
     acc
     (recur f (rest col) (conj acc (f (first col)))))))

((fn recur-map
  ([f col] (recur-map f col []))
  ([f col acc]
   (if (empty? col)
     acc
     (recur f (rest col) (conj acc (f (first col))))))) inc [2 3 4 5])
;;reductions
(fn [f col]
  (drop 1
        (reductions (fn ([a b] (f b))) nil col)))
;; reductions test (blows stack with recur)
(= [1000000 1000001]
   (->> ((fn [f col]
  (drop 1 (reductions (fn ([a b] (f b))) nil col))) inc (range))
        (drop (dec 1000000))
        (take 2)))

;;chouser
(fn l [f [a & m]]
  (lazy-seq
    (cons (f a) (if m (l f m)))))

;;grand/amalloy
(fn m [f s]
  (lazy-seq
    (when-let [[h & t] (seq s)]
      (cons (f h) (m f t)))))

;;noisesmith
(fn MAP [f s]
  (if (empty? s)
    nil
    (lazy-seq
     (cons (f (first s))
           (MAP f (rest s))))))



;; to tree or not to tree # 95
(fn [test-tree]
  (every? true?
   (map #(if-not (sequential? %)
           (if (false? %) false true)
           (if (= (count %) 3)
             true
             false)) (tree-seq sequential? identity test-tree))))

;; sum of square digits #120

;; square
(def sq-sum
  (fn [col] (reduce + (map #(* % %) col))))

(sq-sum [1 2 3])
;; int->string (amalloy)
(def int->str
  (fn [x] (#(map (comp read-string str) (str %)) x)))

(int->str 58)

;; solution submitted using threading last macro
(fn [col]
  (letfn [(sq-sum [c] (reduce + (map #(* % %) c)))
          (int->str [i] (#(map (comp read-string str) (str %)) i))]
    (->> col
         (map (fn [val] (if (< val (sq-sum (int->str val))) 1 0)) )
         (reduce +))))

;;chouser
reduce
#(+ %
    (if (< %2 (apply + (for [c (str %2)]
                         (Math/pow (- (int c) 48) 2))))
      1
      0))
0

;;cgrand
reduce
#(if (< %2 (reduce + 
  (map (zipmap "0123456789" (map * (range) (range)))
    (str %2)))) (inc %) %) 0

;; amalloy
(fn [coll]
  (count
   (for [x coll
         :let [digits (map (comp read-string str) (str x))]
         :when (< x (reduce + (map #(* % %) digits)))]
     x)))

;;noisesmith
#(letfn [(get-digits [n]
           (loop [digits () pool n]
             (if (zero? pool)
               digits
               (recur (cons (rem pool 10) digits) (quot pool 10)))))
         (square [x] (* x x)) 
         (<ssd? [n]
           (< n (apply + (map square (get-digits n)))))]
   (count (filter <ssd? %)))


;; card recognition
(fn [card]
  (let [suits {\S :spade
             \D :diamond
             \H :heart
             \C :club}
      vals (into {} (map vector "23456789TJQKA" (range)))]
    {:suit (get suits (first card)) :rank (get vals (last card))}))

