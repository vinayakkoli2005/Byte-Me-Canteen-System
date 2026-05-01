-- Run this in Supabase SQL Editor (Dashboard > SQL Editor > New query)

create table if not exists menu_items (
  id bigint generated always as identity primary key,
  name text not null unique,
  price numeric(10,2) not null,
  category text not null check (category in ('Snacks','Drinks','Meals')),
  available boolean not null default true,
  created_at timestamptz default now()
);

create table if not exists customers (
  id bigint generated always as identity primary key,
  name text not null,
  roll_number int not null unique,
  is_vip boolean not null default false,
  created_at timestamptz default now()
);

create table if not exists orders (
  id bigint generated always as identity primary key,
  customer_name text not null,
  is_vip boolean not null default false,
  status text not null default 'Preparing',
  delivery_address text,
  payment_method text,
  created_at timestamptz default now()
);

create table if not exists order_items (
  id bigint generated always as identity primary key,
  order_id bigint references orders(id) on delete cascade,
  item_name text not null,
  price numeric(10,2) not null,
  quantity int not null,
  special_request text
);

-- Seed default menu items
insert into menu_items (name, price, category, available) values
  ('Pizza',        10.99, 'Snacks', true),
  ('Burger',        8.99, 'Snacks', true),
  ('Coke',          2.99, 'Drinks', true),
  ('Coffee',        3.99, 'Drinks', true),
  ('Small Meal',   12.99, 'Meals',  true),
  ('Regular Meal',  5.99, 'Meals',  true),
  ('Large Meal',    4.99, 'Meals',  true),
  ('Jumbo Meal',    3.99, 'Meals',  true),
  ('Punjabi Meal',  6.99, 'Meals',  true)
on conflict (name) do nothing;

-- Disable RLS for test mode (enable + add policies before going to production)
alter table menu_items  disable row level security;
alter table customers   disable row level security;
alter table orders      disable row level security;
alter table order_items disable row level security;
