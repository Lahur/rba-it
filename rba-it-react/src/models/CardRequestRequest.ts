import { CardStatus } from './CardStatus'

export interface CardRequestRequest {
    firstName: string
    lastName: string
    oib: string
    status?: CardStatus
}
