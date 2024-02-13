import {Messages} from "../messages/messages";
import {Bid} from "./bid";

export interface Categories{
  id: number;
  name: string;
}

export interface Photos{
  id: number;
  name: string;
  image: any;
  type: string;
}

export interface ItemList {
  results: Item[];
  count: number;
}

export interface Item {
  id: number;
  name: string;
  categories: Categories[];
  buy_price: string;
  currently: number;
  first_bid: number;
  number_of_bids: number;
  location: string;
  latitude: number;
  longitude: number;
  country: string;
  started: string;
  ends: string;
  description: string;
  rating: number;
  userID: string;
  photos: Photos[];
  bids: Bid[];
}
