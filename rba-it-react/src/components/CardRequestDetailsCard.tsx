import type { ReactNode } from 'react'
import type { CardRequestResponse } from '../models/CardRequestResponse'
import { CardStatus } from '../models/CardStatus'

const STATUS_BADGE_STYLES: Record<CardStatus, string> = {
    [CardStatus.PENDING]: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300',
    [CardStatus.APPROVED]: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300',
    [CardStatus.DECLINED]: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300',
}

interface CardRequestDetailsCardProps {
    cardRequest: CardRequestResponse
    title: string
    headerAction?: ReactNode
}

function CardRequestDetailsCard({ cardRequest, title, headerAction }: CardRequestDetailsCardProps) {
    const initials = `${cardRequest.firstName[0] ?? ''}${cardRequest.lastName[0] ?? ''}`.toUpperCase()

    return <div className="max-w-sm mx-auto mt-6 overflow-hidden bg-neutral-primary-soft border border-default rounded-base shadow-xs">
        <div className="flex items-center gap-3 px-6 py-4 border-b border-default bg-neutral-secondary-medium">
            <span className="flex items-center justify-center w-9 h-9 rounded-full bg-brand text-white text-sm font-semibold">
                {initials}
            </span>
            <p className="text-sm font-semibold text-heading">{title}</p>
            {headerAction}
        </div>
        <dl className="px-6 py-4 space-y-3 text-sm">
            <div className="flex items-center justify-between">
                <dt className="text-body">Name</dt>
                <dd className="text-heading">{cardRequest.firstName} {cardRequest.lastName}</dd>
            </div>
            <div className="flex items-center justify-between">
                <dt className="text-body">OIB</dt>
                <dd className="font-mono text-heading">{cardRequest.oib}</dd>
            </div>
            <div className="flex items-center justify-between">
                <dt className="text-body">Status</dt>
                <dd>
                    <span className={`px-2.5 py-0.5 rounded-full text-xs font-medium ${STATUS_BADGE_STYLES[cardRequest.status]}`}>
                        {cardRequest.status}
                    </span>
                </dd>
            </div>
        </dl>
    </div>
}

export default CardRequestDetailsCard