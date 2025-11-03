import api from "./api";

export async function listReceipts(customerAccountId) {
  if (!customerAccountId) {
    return [];
  }
  const res = await api.get("/receipts", { params: { customerId: customerAccountId } });
  return res.data || [];
}

export async function getReceiptById(receiptId) {
  if (!receiptId) {
    throw new Error("receiptId is required");
  }
  const res = await api.get(`/receipts/${receiptId}`);
  return res.data;
}
