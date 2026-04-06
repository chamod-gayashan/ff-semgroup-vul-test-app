// VULN-ID: VB-019 | CWE-95 | Severity: CRITICAL
// eval() with User Input: user-supplied expression string is evaluated directly
// using eval(), allowing arbitrary JavaScript execution in the browser.
// Used in TransferPage to let users type custom fee calculation formulas.
// SAFE VERSION: use a safe math expression parser like mathjs:
//   import { evaluate } from 'mathjs';
//   export function calculateFormula(expression) {
//     return evaluate(expression);
//   }
export function calculateFormula(expression) {
  // eslint-disable-next-line no-eval
  return eval(expression);
}
