package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.effects.stance.StanceParticleHorizontal;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.powers.common.DeenergizedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class AgilityStance extends EYBStance implements OnStartOfTurnSubscriber
{
    public static boolean DeenergizeTriggered = false;
    public static final String STANCE_ID = CreateFullID(AgilityStance.class);
    public static int STAT_GAIN_AMOUNT = 2;
    public static int ENERGY_LOSE_AMOUNT = 1;

    public AgilityStance()
    {
        super(STANCE_ID);
    }

    protected Color GetParticleColor()
    {
        return CreateColor(0.2f, 0.3f, 0.7f, 0.8f, 0.2f, 0.3f);
    }

    protected Color GetAuraColor()
    {
        return CreateColor(0.4f, 0.5f, 0.8f, 0.9f, 0.4f, 0.5f);
    }

    @Override
    public void onEnterStance() {
        super.onEnterStance();

        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.GainDexterity(STAT_GAIN_AMOUNT);
    }

    @Override
    public void onExitStance() {
        super.onExitStance();

        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.GainDexterity(-STAT_GAIN_AMOUNT);
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (info.owner == null || !(info.owner instanceof AbstractPlayer))
        {
            return;
        }

        AbstractPlayer player = (AbstractPlayer)info.owner;

        if (!DeenergizeTriggered && info.type != DamageInfo.DamageType.THORNS && damageAmount - player.currentBlock > 0)
        {
            DeenergizeTriggered = true;
            GameActions.Bottom.ApplyPower(new DeenergizedPower(player, ENERGY_LOSE_AMOUNT));
        }
    }

    @Override
    public void OnStartOfTurn() {
        DeenergizeTriggered = false;
    }

    @Override
    protected void QueueParticle()
    {
        GameEffects.Queue.Add(new StanceParticleHorizontal(GetParticleColor()));
        GameEffects.Queue.Add(new StanceParticleVertical(GetAuraColor()));
    }

    @Override
    protected void QueueAura()
    {
        //GameEffects.Queue.Add(new StanceAura(GetAuraColor()));
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(0.2f, 1f, 0.2f, 1f);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(STAT_GAIN_AMOUNT, ENERGY_LOSE_AMOUNT);
    }
}
