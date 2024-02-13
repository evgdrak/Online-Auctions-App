export interface MessagesList {
  results: Messages[];
  count: number;
}

export interface Messages {
  id: number;
  title: string;
  body: string;
  opened: boolean;
  sender: string;
  receiver: string;
  timestamp: string;
}
