import { CardStatus } from './CardStatus'

export interface CardRequestResponse {
    id: string
    firstName: string
    lastName: string
    oib: string
    status: CardStatus
}
