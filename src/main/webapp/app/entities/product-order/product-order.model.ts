import dayjs from 'dayjs/esm';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

export interface IProductOrder {
  id?: number;
  placedDate?: dayjs.Dayjs;
  status?: OrderStatus;
  code?: string;
  invoices?: IInvoice[] | null;
  orderItems?: IOrderItem[] | null;
  customer?: ICustomer;
}

export class ProductOrder implements IProductOrder {
  constructor(
    public id?: number,
    public placedDate?: dayjs.Dayjs,
    public status?: OrderStatus,
    public code?: string,
    public invoices?: IInvoice[] | null,
    public orderItems?: IOrderItem[] | null,
    public customer?: ICustomer
  ) {}
}

export function getProductOrderIdentifier(productOrder: IProductOrder): number | undefined {
  return productOrder.id;
}
