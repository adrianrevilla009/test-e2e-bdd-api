// Tiny in-memory Orders API (POST /orders, GET /orders/:id, GET /health). No dependencies.
const http = require('node:http');

const orders = new Map();
let nextId = 1;

function send(res, code, obj) {
  res.writeHead(code, { 'Content-Type': 'application/json' });
  res.end(JSON.stringify(obj));
}

http.createServer((req, res) => {
  const url = new URL(req.url, 'http://x');
  if (req.method === 'GET' && url.pathname === '/health') return send(res, 200, { ok: true });
  const one = url.pathname.match(/^\/orders\/(\d+)$/);
  if (req.method === 'GET' && one) {
    const o = orders.get(Number(one[1]));
    return o ? send(res, 200, o) : send(res, 404, { error: 'not found' });
  }
  if (req.method === 'POST' && url.pathname === '/orders') {
    let body = '';
    req.on('data', (c) => (body += c));
    req.on('end', () => {
      let o;
      try { o = JSON.parse(body); } catch { return send(res, 400, { error: 'bad json' }); }
      if (typeof o.sku !== 'string' || !Number.isInteger(o.qty) || o.qty <= 0) return send(res, 400, { error: 'invalid order' });
      const order = { id: nextId++, status: 'NEW', sku: o.sku, qty: o.qty };
      orders.set(order.id, order);
      send(res, 201, order);
    });
    return;
  }
  send(res, 405, { error: 'unsupported' });
}).listen(3000, '0.0.0.0');
