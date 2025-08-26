window.util = {
    /**
     * 
     * @param {{
     *      content: Node[],
     *      wrapper: Node
     * }} config 
     * @returns 
     */
    wrap: (config) => {        
        const toWrapCloned = config.content
            .map(node => node.cloneNode(true))
        const wrapperCloned = config.wrapper.cloneNode(true)
        wrapperCloned.append(...toWrapCloned)
        const firstElement = config.content.at(0)        
        if(!firstElement) {
            throw new Error('One of wrapped elements is not located in the DOM')
        }
        firstElement.parentElement.insertBefore(wrapperCloned, firstElement)
        config.content.forEach(node => node.remove())
        return wrapperCloned
    },
    /**
     * 
     * @param {{
     *      insert: Node,
     *      before: Node
     * }} config 
     * @returns 
     */
    insertBefore: (config) => {
        if(config.before.parentElement) {
            config.before.parentElement.insertBefore(config.insert, config.before)
            return config.insert
        }
        console.error('Element to insert before must have a parent element: ', config.before)
    },
    /**
     * 
     * @param {{
     *      type: string,
     *      classList: string[],
     *      id: number
     * }} config 
     * @returns 
     */
    define: (config) => {
        const elementRef = document.createElement(config.type)
        if(config.classList) {
            elementRef.classList.add(...config.classList)
        }
        if(config.id) {
            elementRef.id = config.id
        }
        return elementRef
    },
    /**
     * 
     * @param {Element} wrapper 
     * @returns {Element[]}
     */
    unwrap: (wrapper) => {
        const children = [...wrapper.children]
        if(wrapper.children) {
            wrapper.replaceWith(...wrapper.children);
            return children
        }
        console.error('Element cannot be unwrapper: ', wrapper)
    },

    /**
     * 
     * @param {{
     *    nodes: Node[],
     *    innerText: string
     * }} config 
     * @returns {Node[][]}
     */
    findAllByInnerText: (config) => {        
        const searchWords = config.innerText
            .toLowerCase()
            .replace(/[^\p{L}\p{N}\s]/gu, '')
            .split(' ');
        const phraseWordNodes = config.nodes
            .map((node, index) => ({
                id: index,
                text: node.innerText.toLowerCase(),
                node: node
            }))
            .filter(node => searchWords.includes(node.text));
        
        const phraseNodes = []
        let buffer = [];
        let pointer = 0;
        for(const node of phraseWordNodes) {
            //check if elements are subsequent
            const lastInBuffer = buffer.at(buffer.length - 1)
            if(lastInBuffer) {
                if(node.id - lastInBuffer.id !== 1) {
                    buffer = []
                    pointer = 0
                }
            }
            //check if subsequent elements match searched phrase
            if(node.text !== searchWords.at(pointer)) {
                buffer = []
                pointer = 0
            }
            buffer.push(node)
            pointer++
            //check if buffer is filled up
            if(buffer.map(word => word.text).join(' ') === searchWords.join(' ')) {                                                
                phraseNodes.push(buffer)
                buffer = []
                pointer = 0
            }
        }
        return phraseNodes
            .map(arr => arr.map(item => item.node))
    }
}