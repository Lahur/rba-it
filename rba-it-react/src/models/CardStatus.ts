export const CardStatus = {
    PENDING: 'PENDING',
    APPROVED: 'APPROVED',
    DECLINED: 'DECLINED',
} as const

export type CardStatus = (typeof CardStatus)[keyof typeof CardStatus]