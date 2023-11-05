#!/usr/bin/env runghc

-- Simple script to generate trig questions for daughter's math practice.

import Control.Arrow
import Data.List

type Angle = Int

data AnglePosition
  = Q1
  | Q2
  | Q3
  | Q4
  | PosX
  | PosY
  | NegX
  | NegY

data Trig = Sin Angle | Cos Angle | Tan Angle

instance Show Trig where
  show (Sin a) = "sin " ++ show a ++ "°"
  show (Cos a) = "cos " ++ show a ++ "°"
  show (Tan a) = "tan " ++ show a ++ "°"

extractAngle :: Trig -> Angle
extractAngle (Sin a) = a
extractAngle (Cos a) = a
extractAngle (Tan a) = a

terminalAngle :: Angle -> Angle
terminalAngle angle
  | angle >= 0 = angle `mod` 360
  | otherwise = (angle `mod` 360) + 360

terminalTrig :: Trig -> Trig
terminalTrig (Sin a) = Sin (terminalAngle a)
terminalTrig (Cos a) = Cos (terminalAngle a)
terminalTrig (Tan a) = Tan (terminalAngle a)

referenceAngle :: Angle -> Angle
referenceAngle angle =
  let modBy180 = terminalAngle angle `mod` 180
   in if modBy180 > 90 then 180 - modBy180 else modBy180

anglePosition :: Angle -> AnglePosition
anglePosition 0 = PosX
anglePosition 90 = PosY
anglePosition 180 = NegX
anglePosition 270 = NegY
anglePosition angle
  | angle > 0 && angle < 90 = Q1
  | angle > 90 && angle < 180 = Q2
  | angle > 180 && angle < 270 = Q3
  | angle > 270 && angle < 360 = Q4
  | otherwise = anglePosition $ terminalAngle angle

trigSign :: Trig -> String
trigSign (Sin angle) = sinSign (anglePosition angle)
  where
    sinSign Q3 = "-"
    sinSign Q4 = "-"
    sinSign NegY = "-"
    sinSign _ = ""
trigSign (Cos angle) = cosSign (anglePosition angle)
  where
    cosSign Q2 = "-"
    cosSign Q3 = "-"
    cosSign NegX = "-"
    cosSign _ = ""
trigSign (Tan angle) = tanSign (anglePosition angle)
  where
    tanSign Q2 = "-"
    tanSign Q4 = "-"
    tanSign _ = ""

trigValue :: Trig -> String
trigValue trig = trigSign trig ++ trigValueHelper trig
  where
    trigValueHelper (Sin angle) = sinValue (referenceAngle angle)
      where
        sinValue 0 = "0"
        sinValue 30 = "1/2"
        sinValue 45 = "√2/2"
        sinValue 60 = "√3/2"
        sinValue 90 = "1"
    trigValueHelper (Cos angle) = cosValue (referenceAngle angle)
      where
        cosValue 0 = "1"
        cosValue 30 = "√3/2"
        cosValue 45 = "√2/2"
        cosValue 60 = "1/2"
        cosValue 90 = "0"
    trigValueHelper (Tan angle) = tanValue (referenceAngle angle)
      where
        tanValue 0 = "0"
        tanValue 30 = "√3/3"
        tanValue 45 = "1"
        tanValue 60 = "√3"
        tanValue 90 = "Inf"

questionString :: Trig -> String
questionString trig = show trig ++ " = "

answerString :: Trig -> String
answerString trig =
  let termTrig = terminalTrig trig
      terminalString =
        if extractAngle trig == extractAngle termTrig
          then ""
          else questionString termTrig
   in questionString trig ++ terminalString ++ trigValue trig

generateAngles :: Int -> Int -> [Int]
generateAngles min max =
  ( concatMap (\n -> map (\a -> a + 90 * n) [0, 30, 45, 60])
      >>> filter (\n -> n >= min && n <= max)
      >>> sort
  )
    [div min 90 .. div max 90]

generateTrigs :: Int -> Int -> [Trig]
generateTrigs min max =
  concatMap (\a -> [Sin a, Cos a, Tan a]) (generateAngles min max)

groupByCols :: Int -> [String] -> [String]
groupByCols n list = map (intercalate ",") (groupByHelper n list [])
  where 
    groupByHelper :: Int -> [a] -> [a] -> [[a]]
    groupByHelper _ [] group = [group]
    groupByHelper 0 list group = group : groupByHelper n list []
    groupByHelper n' (x:xs) group = groupByHelper (n' - 1) xs (group ++ [x])

main = do
  let trigs = generateTrigs 0 360
  putStrLn (unlines $ groupByCols 3 $ map questionString trigs)
  putStrLn (unlines $ groupByCols 3 $ map answerString trigs)
