import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AppService } from 'src/app/_services/app.service';
import { LogService } from 'src/app/_services/log.service';

@Component({
    selector: 'app-upload-file',
    templateUrl: './upload-file.component.html',
    styleUrls: ['./upload-file.component.css']
})
export class UploadFileComponent implements OnInit {
    
    @ViewChild('uploadFile') myInputVariable: ElementRef;
    imgURL: any;
    imgURLUpload: any;
    public message: string;
    currentFile: File = null;
    success = '';
    error = '';
    fileList: Array<any> = [];
    reader = new FileReader();

    constructor(
        private appService: AppService,
        private logger: LogService
    ) { }

    ngOnInit() {
        this.appService.getFiles()
            .subscribe(result => {
                this.fileList = result;
            })
    }

    onSelectFile(event) {
        if (event.target.files.length > 0) {
            this.currentFile = event.target.files[0];
            // var mimeType = event.target.files[0].type;
            // if (mimeType.match(/image\/*/) == null) {
            //     this.message = "Only images are supported.";
            //     return;
            // }

            // Đọc file image hiển thị ra màn hình

            this.reader.readAsDataURL(this.currentFile);
            this.reader.onload = (_event) => {
                this.imgURL = this.reader.result;
            }
        }
    }


    onUpload() {
        this.appService.upload(this.currentFile)
            .subscribe({
                next: (res) => {
                    console.log(res.message);


                    this.imgURLUpload = this.imgURL
                    this.error = '';
                    this.success = res.message;
                },
                error: (err) => {
                    this.success = '';
                    this.error = err;
                }
            })
            , setTimeout(() => {
                this.myInputVariable.nativeElement.value = "";
                this.imgURL = '';
                this.success = '';
            }, 3000);;
    }

}
