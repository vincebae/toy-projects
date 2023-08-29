import { Fraction } from './fraction';

type Degree = {
  degree: number;
};
type Radian = {
  radian: Fraction;
};
type Angle = Degree | Radian;
type AnglePosition =
  | 'Q1'
  | 'Q2'
  | 'Q3'
  | 'Q4'
  | 'PosX'
  | 'NegX'
  | 'PosY'
  | 'NegY';

type Sin = {
  kind: 'sin';
} & Angle;
type Cos = {
  kind: 'cos';
} & Angle;
type Tan = {
  kind: 'tan';
} & Angle;
type Trig = Sin | Cos | Tan;

const range = (start: number, end: number): number[] =>
  Array.from({ length: end - start }, (_, k) => k + start);

const createSin = (angle: Angle): Trig => ({
  kind: 'sin',
  ...angle,
});

const createCos = (angle: Angle): Trig => ({
  kind: 'cos',
  ...angle,
});

const createTan = (angle: Angle): Trig => ({
  kind: 'tan',
  ...angle,
});

const isRadian = (angle: Angle): angle is Radian =>
  (angle as Radian).radian !== undefined;

const radianToDegree = (radian: Radian): Degree => {
  const degree =
    (radian.radian.numeratorComputed() * 180) / radian.radian.denominator();
  return { degree };
};

const degreeToRadian = (degree: Degree): Radian => {
  return { radian: new Fraction(0, degree.degree, 180) };
};

// Returns terminal degree in [0, 360).
const getTerminalDegree = (angle: Angle): Degree => {
  const degree = isRadian(angle) ? radianToDegree(angle) : angle;
  const term =
    degree.degree >= 0 ? degree.degree % 360 : (degree.degree % 360) + 360;
  return { degree: term };
};

// Return reference Degree in [0, 90].
const getReferenceDegree = (angle: Angle): Degree => {
  const modBy180 = getTerminalDegree(angle).degree % 180;
  return { degree: modBy180 > 90 ? 180 - modBy180 : modBy180 };
};

const getAnglePosition = (angle: Angle): AnglePosition => {
  const { degree } = getTerminalDegree(angle);
  switch (degree) {
    case 0:
      return 'PosX';
    case 90:
      return 'PosY';
    case 180:
      return 'NegX';
    case 270:
      return 'NegY';
    default:
      break;
  }
  if (degree < 90) {
    return 'Q1';
  }
  if (degree > 90 && degree < 180) {
    return 'Q2';
  }
  if (degree > 180 && degree < 270) {
    return 'Q3';
  }
  return 'Q4';
};

const sinNegPos: Readonly<Set<AnglePosition>> = Object.freeze(
  new Set(['Q3', 'NegY', 'Q4'] as AnglePosition[]),
);
const cosNegPos: Readonly<Set<AnglePosition>> = Object.freeze(
  new Set(['Q2', 'NegX', 'Q3'] as AnglePosition[]),
);
const tanNegPos: Readonly<Set<AnglePosition>> = Object.freeze(
  new Set(['Q2', 'Q4'] as AnglePosition[]),
);
const trigNegPosMap = {
  sin: sinNegPos,
  cos: cosNegPos,
  tan: tanNegPos,
};

const sinValues: Readonly<Record<number, string>> = {
  0: '0',
  30: '1/2',
  45: '√2/2',
  60: '√3/2',
  90: '1',
};
const cosValues: Readonly<Record<number, string>> = {
  0: '1',
  30: '√3/2',
  45: '√2/2',
  60: '1/2',
  90: '0',
};
const tanValues: Readonly<Record<number, string>> = {
  0: '0',
  30: '√3/3',
  45: '1',
  60: '√3',
  90: 'Inf',
};
const trigValuesMap = {
  sin: sinValues,
  cos: cosValues,
  tan: tanValues,
};

const getSign = (trig: Trig): '' | '-' =>
  trigNegPosMap[trig.kind].has(getAnglePosition(trig)) ? '-' : '';

const getTrigValue = (trig: Trig): string => {
  const reference = getReferenceDegree(trig);
  return getSign(trig) + trigValuesMap[trig.kind][reference.degree];
};

const getQuestionStr = (trig: Trig): string => {
  if (isRadian(trig)) {
    const numerator = trig.radian.numeratorComputed();
    const denominator = trig.radian.denominator();
    return numerator === 0
      ? `${trig.kind} 0 = `
      : denominator !== 1
        ? `${trig.kind} (${numerator} / ${denominator})π = `
        : numerator === 1
          ? `${trig.kind} π = `
          : `${trig.kind} ${numerator}π = `;
  } else {
    return `${trig.kind} ${trig.degree} ° = `;
  }
};

const getAnswerStr = (trig: Trig): string => {
  const questionStr = getQuestionStr(trig);
  const degree = isRadian(trig) ? radianToDegree(trig) : trig;
  const term = getTerminalDegree(trig);
  const terminalStr =
    term.degree !== degree.degree
      ? getQuestionStr({ kind: trig.kind, ...term } as Trig)
      : '';
  const valueStr = getTrigValue(trig);
  return questionStr + terminalStr + valueStr;
};

const generateAngles = (min: number, max: number): Degree[] => {
  const baseAngles: readonly number[] = [0, 30, 45, 60];
  const minDivBy90 = Math.floor(min / 90);
  const maxDivBy90 = Math.floor(max / 90);
  return range(minDivBy90, maxDivBy90 + 1)
    .map((multi) => baseAngles.map((angle) => angle + 90 * multi))
    .reduce((a1, a2) => [...a1, ...a2], [])
    .filter((angle) => angle >= min && angle <= max)
    .sort((n1, n2) => n1 - n2)
    .map((degree: number) => ({ degree } as Degree));
};

const generateTrigs = (min: number, max: number, radian: boolean): Trig[] => {
  const generators = [createSin, createCos, createTan];
  return generateAngles(min, max)
    .map((angle) => (radian ? degreeToRadian(angle as Degree) : angle))
    .map((angle) => generators.map((fn) => fn(angle)))
    .reduce((a1, a2) => [...a1, ...a2], []);
};

const arrayShuffle = <T>(array: T[]): T[] => {
  if (array.length === 0) {
    return [];
  }
  const copied = [...array];
  for (let index = copied.length - 1; index > 0; index--) {
    const newIndex = Math.floor(Math.random() * (index + 1));
    [copied[index], copied[newIndex]] = [copied[newIndex], copied[index]];
  }
  return copied;
};

const groupByN = (arr: string[], n: number): string[] => {
  return range(0, Math.ceil(arr.length / n))
    .map((i) => arr.slice(i * n, (i + 1) * n))
    .map((row) => row.join(','));
};

const generatesWorksheet = (
  min: number,
  max: number,
  cols: number,
  radian: boolean,
  shuffle: boolean,
): string => {
  const trigs = (shuffle ? arrayShuffle : <T>(x: T): T => x)(
    generateTrigs(min, max, radian),
  );
  const questions = groupByN(trigs.map(getQuestionStr), cols).join('\n');
  const answers = groupByN(trigs.map(getAnswerStr), cols).join('\n');
  return questions + '\n' + answers;
};

const worksheet1 = generatesWorksheet(0, 360, 3, true, false);
const worksheet2 = generatesWorksheet(0, 360, 3, true, true);
console.log(worksheet1);
console.log('='.repeat(80));
console.log(worksheet2);
