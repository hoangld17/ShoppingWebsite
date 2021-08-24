import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
declare var $: any;
@Component({
  selector: 'app-modal-cart',
  templateUrl: './modal-cart.component.html',
  styleUrls: ['./modal-cart.component.css']
})
export class ModalCartComponent implements OnInit {
  @Input() address!: string;
  @Input() phone!: string;
  @Output() edit = new EventEmitter<{address: string, phone: string}>();
  cartForm!: FormGroup;
  submitted = false;
  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.cartForm = this.formBuilder.group({
      address: [this.address, [Validators.required]],
      phone: [this.phone, [Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/), Validators.minLength(10)]]
    });
  }
  get ff() { return this.cartForm.controls; }
  onSubmit(){
    this.submitted = true;
    if (this.cartForm.invalid){
      return;
    }
    $('#cartmodal').modal('hide');
    this.edit.emit(this.cartForm.value);
  }
  closeCart(){
    this.submitted = false;
    $('#cartmodal').modal('hide');
  }
}
