package pinacolada.cards.eternal.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.utilities.PCLActions;

public class Defend extends EternalCard
{
    public static final PCLCardData DATA = Register(Defend.class).SetSkill(1, CardRarity.BASIC, PCLCardTarget.None);

    public Defend()
    {
        super(DATA);

        Initialize(0, 5);
        SetUpgrade(0, 3);

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CheckPrimaryCondition(true)) {
            PCLActions.Bottom.StackPower(new NextTurnBlockPower(p, block));
        }
        else {
            PCLActions.Bottom.GainBlock(block);
        }
    }
}