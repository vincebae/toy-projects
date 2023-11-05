export class Fraction {
  private _whole: number;
  private _numerator: number;
  private _denominator: number;

  constructor(w: number, n: number, d: number) {
    this._whole = w + Math.floor(n / d);
    const gcd = this.gcd(n % d, d);
    this._numerator = (n % d) / gcd;
    this._denominator = d / gcd;
  }

  whole() {
    return this._whole;
  }

  denominator() {
    return this._denominator;
  }

  numerator() {
    return this._numerator;
  }

  numeratorComputed() {
    return this._whole * this._denominator + this._numerator;
  }

  private gcd(n1: number, n2: number): number {
    return n2 == 0 ? n1 : this.gcd(n2, n1 % n2);
  }
}

