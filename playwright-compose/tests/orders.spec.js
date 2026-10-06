const { test, expect } = require('@playwright/test');

// Test data strategy: unique sku per test, and each test creates the order it reads,
// so tests can run in parallel against the shared compose stack without interfering.
const sku = () => `SKU-${test.info().testId}`;

test('creates an order', async ({ request }) => {
  const res = await request.post('/orders', { data: { sku: sku(), qty: 2 } });
  expect(res.status()).toBe(201);
  expect(await res.json()).toMatchObject({ status: 'NEW', qty: 2 });
});

test('fetches a created order', async ({ request }) => {
  const created = await (await request.post('/orders', { data: { sku: sku(), qty: 3 } })).json();
  const res = await request.get(`/orders/${created.id}`);
  expect(res.status()).toBe(200);
  expect((await res.json()).qty).toBe(3);
});

test('rejects invalid quantity', async ({ request }) => {
  const res = await request.post('/orders', { data: { sku: sku(), qty: 0 } });
  expect(res.status()).toBe(400);
});

test('unknown order is 404', async ({ request }) => {
  expect((await request.get('/orders/99999')).status()).toBe(404);
});
