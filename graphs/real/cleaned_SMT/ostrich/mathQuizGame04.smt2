(set-logic QF_S)
(set-option :parse-transducers true)
(set-option :produce-models true)
(define-fun-rec toLower ((x String) (y String)) Bool
   (or (and (= x "") (= y ""))
       (and (not (= x "")) (not (= y ""))
           (= (char.code (str.head y))
               (ite (and (<= 65 (char.code (str.head x)))
                       (<= (char.code (str.head x)) 90))
                   (+ (char.code (str.head x)) 32)
                   (char.code (str.head x))))
           (toLower (str.tail x) (str.tail y))))
)
(declare-fun r2 () String)
(declare-fun L0  () String)
(assert (toLower r2  L0 ))

(assert (not (str.contains L0 "/setsize" )))
(assert (not (= L0 "/quit" )))
(assert (= L0 "/clear" ))
(assert (not (= L0 "y" )))
(assert (not (= L0 "/restart" )))
(assert (not (= L0 "/help" )))
(assert (not (= L0 "/quit" )))
(assert (not (str.contains L0 "/say" )))
(assert (= L0 "high school" ))
(assert (not (= L0 "middle school" )))
(assert (not (= L0 "n" )))
(assert (not (= L0 "/restart" )))
(assert (not (= L0 "elementary" )))
(assert (not (str.contains L0 "/setfont" )))
(assert (not (= L0 "/clear" )))
(assert (not (= L0 "y" )))
(assert (not (= L0 "/?" )))
(assert (not (= L0 "n" )))
(check-sat)
(get-model)
(exit)
