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
(declare-fun r2 () String)

(assert (= (str.toLower r2 )"/quit" ))
(assert (not (= (str.toLower r2 )"middle school" )))
(assert (= (str.toLower r2 )"high school" ))
(assert (not (= (str.toLower r2 )"y" )))
(assert (not (= (str.toLower r2 )"n" )))
(assert (not (= (str.toLower r2 )"/restart" )))
(assert (not (= (str.toLower r2 )"elementary" )))
(check-sat)
(get-model)
(exit)
