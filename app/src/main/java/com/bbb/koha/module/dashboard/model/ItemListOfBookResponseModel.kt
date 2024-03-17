package com.bbb.koha.module.dashboard.model

import com.bbb.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbb.koha.module.my_account.summary.model.ItemDetailResponseModel
import com.google.gson.annotations.SerializedName

data class ItemListOfBookResponseModel(@SerializedName("acquisition_date"                  ) var acquisitionDate               : String?  = "",
                                       @SerializedName("acquisition_source"                ) var acquisitionSource             : String?  = "",
                                       @SerializedName("biblio_id"                         ) var biblioId                      : Int?     = null,
                                       @SerializedName("call_number_sort"                  ) var callNumberSort                : String?  = "",
                                       @SerializedName("call_number_source"                ) var callNumberSource              : String?  = "",
                                       @SerializedName("callnumber"                        ) var callnumber                    : String?  = "",
                                       @SerializedName("checked_out_date"                  ) var checkedOutDate                : String?  = "",
                                       @SerializedName("checkouts_count"                   ) var checkoutsCount                : Int?     = null,
                                       @SerializedName("coded_location_qualifier"          ) var codedLocationQualifier        : String?  = "",
                                       @SerializedName("collection_code"                   ) var collectionCode                : String?  = "",
                                       @SerializedName("copy_number"                       ) var copyNumber                    : String?  = "",
                                       @SerializedName("damaged_date"                      ) var damagedDate                   : String?  = "",
                                       @SerializedName("damaged_status"                    ) var damagedStatus                 : Int?     = null,
                                       @SerializedName("effective_item_type_id"            ) var effectiveItemTypeId           : String?  = "",
                                       @SerializedName("effective_not_for_loan_status"     ) var effectiveNotForLoanStatus     : Int?     = null,
                                       @SerializedName("exclude_from_local_holds_priority" ) var excludeFromLocalHoldsPriority : Boolean? = null,
                                       @SerializedName("extended_subfields"                ) var extendedSubfields             : String?  = "",
                                       @SerializedName("external_id"                       ) var externalId                    : String?  = "",
                                       @SerializedName("holding_library_id"                ) var holdingLibraryId              : String?  = "",
                                       @SerializedName("holds_count"                       ) var holdsCount                    : String?  = "",
                                       @SerializedName("home_library_id"                   ) var homeLibraryId                 : String?  = "",
                                       @SerializedName("internal_notes"                    ) var internalNotes                 : String?  = "",
                                       @SerializedName("inventory_number"                  ) var inventoryNumber               : String?  = "",
                                       @SerializedName("item_id"                           ) var itemId                        : Int?     = null,
                                       @SerializedName("item_type_id"                      ) var itemTypeId                    : String?  = "",
                                       @SerializedName("last_checkout_date"                ) var lastCheckoutDate              : String?  = "",
                                       @SerializedName("last_seen_date"                    ) var lastSeenDate                  : String?  = "",
                                       @SerializedName("location"                          ) var location                      : String?  = "",
                                       @SerializedName("lost_date"                         ) var lostDate                      : String?  = "",
                                       @SerializedName("lost_status"                       ) var lostStatus                    : Int?     = null,
                                       @SerializedName("materials_notes"                   ) var materialsNotes                : String?  = "",
                                       @SerializedName("new_status"                        ) var newStatus                     : String?  = "",
                                       @SerializedName("not_for_loan_status"               ) var notForLoanStatus              : Int?     = null,
                                       @SerializedName("permanent_location"                ) var permanentLocation             : String?  = "",
                                       @SerializedName("public_notes"                      ) var publicNotes                   : String?  = "",
                                       @SerializedName("purchase_price"                    ) var purchasePrice                 : Int?     = null,
                                       @SerializedName("renewals_count"                    ) var renewalsCount                 : String?  = "",
                                       @SerializedName("replacement_price"                 ) var replacementPrice              : Int?     = null,
                                       @SerializedName("replacement_price_date"            ) var replacementPriceDate          : String?  = "",
                                       @SerializedName("restricted_status"                 ) var restrictedStatus              : String?  = "",
                                       @SerializedName("serial_issue_number"               ) var serialIssueNumber             : String?  = "",
                                       @SerializedName("timestamp"                         ) var timestamp                     : String?  = "",
                                       @SerializedName("uri"                               ) var uri                           : String?  = "",
                                       @SerializedName("withdrawn"                         ) var withdrawn                     : Int?     = null,
                                       @SerializedName("withdrawn_date"                    ) var withdrawnDate                 : String?  = "",
                                       var itemDetailResponseModel: ItemDetailResponseModel?=null,
                                       var checkoutResponseModel: CheckoutResponseModel?=null
)
