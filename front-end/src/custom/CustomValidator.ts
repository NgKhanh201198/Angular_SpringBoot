import { AbstractControl, ValidatorFn } from "@angular/forms";

export const emailValidator = (): ValidatorFn => {
    return (control: AbstractControl): { [key: string]: string } => {
        const result = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(control.value);
        // console.log(`emailValidator = ${result}`);
        return result == true ? null : { 'Error': "Wrong email format" };
    };
}
export const passwordValidator = (): ValidatorFn => {
    return (control: AbstractControl): { [key: string]: string } => {
        const result = /^[a-zA-Z0-9_@]{6,18}$/i.test(control.value);
        // console.log(`emailValidator = ${result}`);
        return result == true ? null : { 'Error': "Invalid password" };
    };
}