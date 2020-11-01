<h1>Internet Data Router Simulation</h1>
<h2>Final Mini-Project, Data Structures, Spring 2020</h2>

<p>IP Addresses are routed to ports based on rules the user adds to the router. IP Addresses can then be injected, and routing decisions are made based on the rules.</p>
<p>The router is encapsulated by a single class, IPRouter, which uses an instance of the Trie class for prefix matching, and an instance of the RouteCache class for its LRU cache, which is updated after an IP Address is routed.</p>

