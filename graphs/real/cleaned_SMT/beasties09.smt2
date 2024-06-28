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
(declare-fun r4 () String)

(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"r" )))
(assert (not (= "" r4 )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (= (str.toLower (str.substr r4  0 1))"h" ))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= "" "q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"g" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= "" r4 )))
(assert (= "y" "y" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= "" r4 )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= "y" "n" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (= "" "" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= "" r4 )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= "" r4 )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"r" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"g" )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= "" r4 )))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (= (str.toLower (str.substr r4  0 1))"h" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"g" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"g" )))
(assert (not (= "" r4 )))
(assert (not (= "" r4 )))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= "" r4 )))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= "" "y" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (= (str.toLower (str.substr r4  0 1))"y" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"r" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (= (str.toLower (str.substr r4  0 1))"l" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"g" )))
(assert (= (str.toLower (str.substr r4  0 1))"l" ))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"r" )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"r" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"g" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"q" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= "" "y" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (= (str.toLower (str.substr r4  0 1))"l" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"l" )))
(assert (not (= "" (str.toLower (str.substr r4  0 1)))))
(assert (= (str.toLower (str.substr r4  0 1))"g" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"r" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"l" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= "" r4 )))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (= (str.toLower (str.substr r4  0 1))"l" ))
(assert (not (= (str.toLower (str.substr r4  0 1))"k" )))
(assert (not (= (str.toLower (str.substr r4  0 1))"p" )))
(check-sat)
(get-model)
(exit)
