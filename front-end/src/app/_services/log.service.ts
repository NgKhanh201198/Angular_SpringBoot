import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";

@Injectable()

export class LogService {
    isLog: boolean = environment.isLog;

    log(msg: any) {
        if (this.isLog == true) {
            console.log("Message: " + JSON.stringify(msg));
        } return false;
    }

    logError(msg: any) {
        if (this.isLog == true) {
            console.error("Error: " + JSON.stringify(msg));
        } return false;
    }

    logWarn(msg: any) {
        if (this.isLog == true) {
            console.warn("Warning: " + JSON.stringify(msg));
        } return false;
    }

    logData(msg: any) {
        if (this.isLog == true) {
            console.log(msg);
        } return false;
    }
}