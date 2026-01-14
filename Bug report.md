# Bug Report: Discount is calculated incorrectly when applied after adding multiple items

**Priority:** High  
**Severity:** Major  

---

## Environment
- **API endpoint:** `/cart/{cartId}/discount`
- **HTTP method:** POST + subsequent GET `/cart/{cartId}`
- **Affected discount codes:** SAVE10, SAVE20, HALF

---

## Preconditions
- Cart is created
- At least one item is added to the cart
- Discount code is applied
- Cart is retrieved via GET `/cart/{cartId}`

---

## Description
The service calculates the discount **only based on the first item** added to the cart, instead of using the **current subtotal** that includes all items. Because of this, the returned discount is always lower than expected when the cart contains multiple items. This behavior happens consistently for all tested discount codes.

---

## Steps to Reproduce
1. Create a new cart
2. Add one or more items to the cart
3. Apply discount code (SAVE10, SAVE20, or HALF)
4. (Optional) Add more items after applying the discount
5. GET `/cart/{cartId}`
6. Check the returned discount and total values

---

## Actual vs Expected (based on failing tests)

### Case 1 — SAVE10 (recalculation scenario)
**Items:**
- Apple: 100 × 1
- Banana: 50 × 2  

**Expected subtotal:** 200  
**Expected discount:** 20 (10% of 200)  
**Actual discount:** 10  

### Case 2 — HALF (fractional boundary case)
**Items:**
- Apple: 33 × 3 = 99
- Banana: 25 × 2 = 50  

**Subtotal:** 149  
**Expected discount:** 74.5  
**Actual discount:** 49.5  

### Case 3 — SAVE20 (multiple items)
**Items:**
- Apple: 50 × 2 = 100
- Banana: 25 × 4 = 100  

**Expected subtotal:** 200  
**Expected discount:** 40 (20% of 200)  
**Actual discount:** 20  

---

## Root Cause
It seems the service doesn’t recalculate the discount after adding more items.

---

## Expected Behavior (Acceptance Criteria)
After applying a discount code:
- The discount should always be recalculated based on the **current cart subtotal**
- GET `/cart/{cartId}` should return:
  - **subtotal:** sum of all items currently in cart
  - **discount:** correct percentage of current subtotal
  - **total:** subtotal − discount
