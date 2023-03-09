package no.sb1.open.api.example.adapters

data class AccountData(
    val errors: List<String>,
    val accounts: List<Account>,
    val linksDTO: LinksDTO
)

data class Account(
    val key: String,
    val iban: String,
    val name: String,
    val type: String,
    val owner: Owner,
    val balance: Double,
    val linksDTO: LinksDTO,
    val obsCodes: List<String>,
    val productId: String,
    val description: String,
    val productType: String,
    val currencyCode: String,
    val disposalRole: Boolean,
    val accountNumber: String,
    val descriptionCode: String,
    val availableBalance: Double,
    val accountProperties: AccountProperties,
    val creditCardAccountID: String,
    val creditCardCreditLimit: Double,
    val eInvoiceCustomerReference: String
)

data class Owner(
    val age: Any,
    val name: String,
    val ssnKey: String,
    val lastName: String,
    val firstName: String,
    val customerKey: String,
    val organisationNumber: String
)

data class LinksDTO(
    val links: Any
)

data class AccountProperties(
    val isOwned: Boolean,
    val isHidden: Boolean,
    val hasAccess: Boolean,
    val isBlocked: Boolean,
    val isFlexiLoan: Boolean,
    val isBonusAccount: Boolean,
    val isCodebitorLoan: Boolean,
    val isSavingsAccount: Boolean,
    val isAksjesparekonto: Boolean,
    val isSecurityBalance: Boolean,
    val isBalancePreferred: Boolean,
    val isTransferToEnabled: Boolean,
    val isPaymentFromEnabled: Boolean,
    val isWithdrawalsAllowed: Boolean,
    val userHasRightOfAccess: Boolean,
    val isAllowedInAvtaleGiro: Boolean,
    val isTransferFromEnabled: Boolean,
    val userHasRightOfDisposal: Boolean,
    val isDefaultPaymentAccount: Boolean,
    val isBalanceUpdatedImmediatelyOnTransferTo: Boolean
)
