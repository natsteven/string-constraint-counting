(set-logic ALL)
(set-option :produce-models true)
(define-fun-rec str.toLower ((x String)) String
  (ite (= x "")
      ""
      (let ((Head (str.at x 0)))
        (str.++ 
          (ite (and (<= 65 (str.to_code Head)) 
                     (<= (str.to_code Head) 90))
               (str.from_code (+ (str.to_code Head) 32))
               Head)
          (str.toLower (str.substr x 1 (- (str.len x) 1)))))))
(declare-fun r1[$i2] () String)
(declare-fun $r3 () String)

(assert (= (str.toLower $r3 )(str.toLower "name" )))
(assert (not (= (str.toLower $r3 )(str.toLower "type" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "length" ))))
(assert (= (str.toLower $r3 )(str.toLower "database" )))
(assert (not (= (str.toLower $r3 )(str.toLower "description" ))))
(assert (= (str.toLower $r3 )(str.toLower "option" )))
(assert (not (= (str.toLower $r3 )(str.toLower "description" ))))
(assert (= (str.toLower $r3 )(str.toLower "description" )))
(assert (not (= (str.toLower $r3 )(str.toLower "description" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "description" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "description" ))))
(assert (= (str.toLower $r3 )(str.toLower "name" )))
(assert (= (str.toLower $r3 )(str.toLower "name" )))
(assert (not (= (str.toLower $r3 )(str.toLower "description" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (not (= (str.toLower r1[$i2] )"sql" )))
(assert (= (str.toLower $r3 )(str.toLower "name" )))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (= (str.toLower $r3 )(str.toLower "description" )))
(assert (= (str.toLower $r3 )(str.toLower "description" )))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (= (str.toLower $r3 )(str.toLower "option" )))
(assert (not (= (str.toLower $r3 )(str.toLower "description" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (= (str.toLower r1[$i2] )"html" ))
(assert (not (= (str.toLower $r3 )(str.toLower "description" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (= (str.toLower $r3 )(str.toLower "type" )))
(assert (not (= (str.toLower $r3 )(str.toLower "type" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (= (str.toLower $r3 )(str.toLower "field" )))
(assert (= (str.toLower $r3 )(str.toLower "length" )))
(assert (= (str.toLower $r3 )(str.toLower "table" )))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "description" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "length" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "description" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "table" ))))
(assert (= (str.toLower $r3 )(str.toLower "name" )))
(assert (not (= (str.toLower $r3 )(str.toLower "description" ))))
(assert (= (str.toLower $r3 )(str.toLower "field" )))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (= (str.toLower $r3 )(str.toLower "description" )))
(assert (not (= (str.toLower $r3 )(str.toLower "field" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (= (str.toLower $r3 )(str.toLower "name" )))
(assert (not (= (str.toLower $r3 )(str.toLower "type" ))))
(assert (= (str.toLower $r3 )(str.toLower "option" )))
(assert (not (= (str.toLower $r3 )(str.toLower "type" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (not (= (str.toLower $r3 )(str.toLower "name" ))))
(assert (= (str.toLower $r3 )(str.toLower "type" )))
(assert (not (= (str.toLower $r3 )(str.toLower "length" ))))
(check-sat)
(get-model)
(exit)
