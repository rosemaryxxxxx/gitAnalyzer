package cn.xhh.tool.sdk.infrastructure.openai;

import cn.xhh.tool.sdk.infrastructure.openai.dto.ChatCompletionRequestDTO;
import cn.xhh.tool.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;

/**
 * @Author rosemaryxxxxx
 * @Date 2024/12/30 16:14
 * @PackageName:cn.xhh.tool.sdk.infrastructure.openai.dto
 * @ClassName: IOpenAI
 * @Description: TODO
 * @Version 1.0
 */
public interface IOpenAI {

    ChatCompletionSyncResponseDTO completions(ChatCompletionRequestDTO requestDTO) throws Exception;
}
