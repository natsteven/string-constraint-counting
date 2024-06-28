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
(declare-fun $r3 () String)
(declare-fun L23  () String)
(declare-fun L45  () String)
(declare-fun L13  () String)
(declare-fun L57  () String)
(declare-fun L9  () String)
(declare-fun L11  () String)
(declare-fun L3  () String)
(declare-fun L1  () String)
(declare-fun L0  () String)
(assert (toLower $r3  L0 ))
(assert (toLower "name"  L1 ))
(assert (toLower "length"  L3 ))
(assert (toLower "type"  L11 ))
(assert (toLower "description"  L9 ))
(assert (toLower "option"  L57 ))
(assert (toLower "field"  L13 ))
(assert (toLower "database"  L45 ))
(assert (toLower "table"  L23 ))

(assert (not (= L0 L1 )))
(assert (= L0 L3 ))
(assert (not (= L0 L1 )))
(assert (= L0 L1 ))
(assert (not (= L0 L9 )))
(assert (= L0 L11 ))
(assert (= L0 L13 ))
(assert (not (= L0 L3 )))
(assert (= L0 L1 ))
(assert (not (= L0 L9 )))
(assert (not (= L0 L9 )))
(assert (not (= L0 L23 )))
(assert (not (= L0 L9 )))
(assert (not (= L0 L1 )))
(assert (not (= L0 L1 )))
(assert (not (= L0 L9 )))
(assert (not (= L0 L9 )))
(assert (not (= L0 L9 )))
(assert (not (= L0 L1 )))
(assert (not (= L0 L9 )))
(assert (= L0 L9 ))
(assert (not (= L0 L11 )))
(assert (= L0 L45 ))
(assert (= L0 L9 ))
(assert (not (= L0 L3 )))
(assert (not (= L0 L3 )))
(assert (not (= L0 L9 )))
(assert (= L0 L11 ))
(assert (= L0 L57 ))
(assert (not (= L0 L1 )))
(assert (not (= L0 L1 )))
(assert (= L0 L57 ))
(assert (not (= L0 L1 )))
(assert (not (= L0 L1 )))
(assert (not (= L0 L1 )))
(assert (not (= L0 L1 )))
(assert (= L0 L1 ))
(assert (= L0 L57 ))
(assert (= L0 L13 ))
(assert (not (= L0 L9 )))
(assert (= L0 L1 ))
(assert (= "sql" "sql" ))
(assert (= L0 L9 ))
(assert (not (= L0 L1 )))
(assert (not (= L0 L11 )))
(assert (not (= L0 L1 )))
(assert (not (= L0 L13 )))
(assert (not (= L0 L9 )))
(assert (= L0 L9 ))
(assert (not (= L0 L1 )))
(assert (= L0 L23 ))
(assert (not (= L0 L1 )))
(assert (not (= L0 L11 )))
(assert (not (= L0 L11 )))
(check-sat)
(get-model)
(exit)
